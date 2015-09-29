package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.*;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.OA;
import edu.virginia.psyc.pi.persistence.Questionnaire.OARepository;
import edu.virginia.psyc.pi.service.EmailService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    @Autowired
    private TangoService tangoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OARepository oaRepository;

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

        Participant p = getParticipant(principal);
        Study study  = p.getStudy();
        Session session = study.getCurrentSession();
        Session last    = study.getLastSession();

        // Provide dates for next visit.
        DateTime startDate = new DateTime(p.lastMilestone()).plusDays(2);
        DateTime endDate = new DateTime(p.lastMilestone()).plusDays(5);
        DateTime completeBy = new DateTime(p.lastMilestone()).plusDays(2);
        DateTimeFormatter startFormat = DateTimeFormat.forPattern("MMMM d - ");
        DateTimeFormatter endFormat = DateTimeFormat.forPattern("MMMM d, YYYY");

        // Determine if a gift should be awarded, and award it.
        if(last.isAwardGift() && !p.giftAwardedForSession(last)) {
            Reward reward = tangoService.createGiftCard(p, last.getName());
            this.emailService.sendGiftCardEmail(p, reward);
            model.addAttribute("giftAwarded", true);
        }

        model.addAttribute("participant", p);
        model.addAttribute("lastSession", study.getLastSession());
        model.addAttribute("currentSession", session);
        model.addAttribute("nextGiftSession", study.nextGiftSession());
        model.addAttribute("currentTask", session.getCurrentTask());
        model.addAttribute("sessionState", study.getState().toString());
        model.addAttribute("dateRange", startFormat.print(startDate) + endFormat.print(endDate));
        model.addAttribute("completeBy", endFormat.print(completeBy));

        return "home";
    }

    @RequestMapping("/overview")
    public String overview(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        Study study  = p.getStudy();

        model.addAttribute("participant", p);
        model.addAttribute("study", study);

        return "overview";
    }

    @RequestMapping("/graph")
    public String graph(ModelMap model, Principal principal) {

        ParticipantDAO dao = getParticipantDAO(principal.getName());
        Participant p      = getParticipant(dao);
        List<OA> oaList    = oaRepository.findByParticipantDAO(dao);
        List<List<Object>> points = new ArrayList();
        List<List<Object>> regressionPoints = new ArrayList();

        Collections.sort(oaList);
        SimpleRegression regression;

        OA original = oaList.get(0);
        OA last     = oaList.get(oaList.size() - 1);

        regression = OA.regression(oaList);

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

        Participant p = getParticipant(principal);
        Study study = p.getStudy();

        // Re-direct to the next step the current session is in progress.
        if(study.getState() == Study.STUDY_STATE.IN_PROGRESS) {
            return new RedirectView(study.getCurrentSession().getCurrentTask().getRequestMapping());
        } else {
            return new RedirectView("/session");
        }
    }

}


