package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.*;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.OA;
import edu.virginia.psyc.pi.persistence.Questionnaire.OARepository;
import edu.virginia.psyc.pi.service.EmailService;
import edu.virginia.psyc.pi.service.ExportService;
import edu.virginia.psyc.pi.service.TangoService;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import edu.virginia.psyc.mindtrails.domain.Session;
import edu.virginia.psyc.mindtrails.domain.Study;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/session")
public class SessionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    @Autowired private TangoService tangoService;
    @Autowired private EmailService emailService;
    @Autowired private OARepository oaRepository;
    @Autowired private ExportService exportService;


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public SessionController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @RequestMapping("")
    public String sessionHome(ModelMap model, Principal principal) throws Exception {

        PiParticipant p = getParticipant(principal);
        Study study = p.getStudy();
        Session session = study.getCurrentSession();
        Session last = study.getLastSession();

        // Provide dates for next visit.
        DateTime startDate = new DateTime(p.lastMilestone()).plusDays(2);
        DateTime endDate = new DateTime(p.lastMilestone()).plusDays(5);
        DateTime completeBy = new DateTime(p.lastMilestone()).plusDays(2);
        DateTimeFormatter startFormat = DateTimeFormat.forPattern("MMMM d - ");
        DateTimeFormatter endFormat = DateTimeFormat.forPattern("MMMM d, YYYY");

        model.addAttribute("participant", p);
        model.addAttribute("lastSession", study.getLastSession());
        model.addAttribute("currentSession", session);
        model.addAttribute("nextGiftSession", study.nextGiftSession());
        model.addAttribute("currentTask", session.getCurrentTask());
        model.addAttribute("sessionState", study.getState().toString());
        model.addAttribute("dateRange", startFormat.print(startDate) + endFormat.print(endDate));
        model.addAttribute("completeBy", endFormat.print(completeBy));

        // Don't allow people to progress if we reach the max form submissions
        // and the exports are not running correctly.
        if((study.getState() == Study.STUDY_STATE.READY ||
                study.getState() == Study.STUDY_STATE.IN_PROGRESS) &&
                exportService.disableAdditionalFormSubmissions() == true) {
            LOG.error("The site is disabled.  We have " + exportService.totalDeleteableRecords() +
                    " total questionnaire submissions. It has been " + exportService.minutesSinceLastExport()
                    + " minutes since the last export ran.");
            return "sessionHome/siteDisabled";

        }

        // Determine if a gift should be awarded, and award it.
        if (last.getGiftAmount() > 0 && !p.giftAwardedForSession(last)) {
            Reward reward = tangoService.createGiftCard(p, last.getName(), last.getGiftAmount());
            this.emailService.sendGiftCardEmail(p, reward, last.getGiftAmount());
            model.addAttribute("giftAwarded", true);
            model.addAttribute("giftAmount", last.getGiftAmount()/100);
        }

        switch (study.getState()) {
            case IN_PROGRESS:
                return "sessionHome/inProgress"; // check
            case ALL_DONE:
                return "sessionHome/allDone"; // check
            case READY:
                return "sessionHome/ready";       // Check
            case WAIT_A_DAY:
                return "sessionHome/waitADay"; // check
            case WAIT_FOR_FOLLOWUP:
                return "sessionHome/waitFollowup";  
            default:
                return "sessionHome/inProgress";
        }
    }

    @RequestMapping("/overview")
    public String overview(ModelMap model, Principal principal) {
        PiParticipant p = getParticipant(principal);
        Study study  = p.getStudy();

        model.addAttribute("participant", p);
        model.addAttribute("study", study);
        model.addAttribute("complete", p.getStudy().getCurrentSession().getName().equals("COMPLETE"));

        return "overview";
    }

    @RequestMapping("/graph")
    public String graph(ModelMap model, Principal principal) {

        ParticipantDAO dao = getParticipantDAO(principal.getName());
        PiParticipant p      = getParticipant(dao);
        List<OA> oaList    = oaRepository.findByParticipantDAO(dao);
        List<List<Object>> points = new ArrayList();
        List<List<Object>> regressionPoints = new ArrayList();

        Collections.sort(oaList);
        SimpleRegression regression;

        OA original = oaList.get(0);
        OA last     = oaList.get(oaList.size() - 1);

        regression = new SimpleRegression();
        double counter = 0;
        for(OA oa : oaList) {
                // don't include the post assessment when calculating the regression.
                 if (!oa.getSession().startsWith("POST")) {
                    regression.addData(counter, oa.score());
                    counter++;
                }
            }

        // Create plot points
        List<Object> point;
        for(OA oa : oaList) {
            point = new ArrayList<>();
            point.add(CBMStudy.calculateDisplayName(oa.getSession()));
            point.add(oa.score());
            points.add(point);
            if(oa.equals(original)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.getIntercept());
                regressionPoints.add(rPoint);
            }
            if(oa.equals(last)) {
                ArrayList<Object> rPoint = new ArrayList<>(point);
                rPoint.set(1, regression.predict(oaList.size()));
                regressionPoints.add(rPoint);
            }
        }

        int improvement = new Double((regression.getIntercept() - regression.predict(oaList.size()))/regression.getIntercept() * 100).intValue();
        String status = "";
        if(Math.abs(improvement) < 15) status = "same";
        else if (improvement > 30) status = "lot";
        else if (improvement > 15) status = "little";
        else if (improvement < -15) status = "worse";

        model.addAttribute("participant", p);
        model.addAttribute("points", points);
        model.addAttribute("regressionPoints", regressionPoints);
        model.addAttribute("improvement", improvement);
        model.addAttribute("status", status);

        return "graph";
    }

    @RequestMapping("/next")
    public View nextStepInSession(ModelMap model, Principal principal) {

        PiParticipant p = getParticipant(principal);
        Study study = p.getStudy();

        // Re-direct to the next step the current session is in progress.
        if(study.getState() == Study.STUDY_STATE.IN_PROGRESS) {
            return new RedirectView(study.getCurrentSession().getCurrentTask().getRequestMapping());
        } else {
            return new RedirectView("/session");
        }
    }

    @RequestMapping("/atRisk")
    public String atRisk(ModelMap model, Principal principal) {

        PiParticipant p = getParticipant(principal);
        Study study = p.getStudy();
        model.addAttribute("participant", p);
        model.addAttribute("study", study);
        return "atRisk";
    }

}


