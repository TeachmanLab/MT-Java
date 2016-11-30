package org.mindtrails.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.ExportService;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.TangoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

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
    @Autowired private ExportService exportService;
    @Autowired private ParticipantService participantService;


    @RequestMapping("")
    public String sessionHome(ModelMap model, Principal principal) throws Exception {

        Participant p = getParticipant(principal);
        Study study = p.getStudy();
        Session session = study.getCurrentSession();
        Session last = study.getLastSession();

        // Provide dates for next visit.
        DateTime startDate = new DateTime(p.lastMilestone()).plusDays(session.getDaysToWait());
        DateTime endDate = new DateTime(p.lastMilestone()).plusDays(session.getDaysToWait() + 2);
        DateTime completeBy = new DateTime(p.lastMilestone()).plusDays(session.getDaysToWait() + 2);
        DateTimeFormatter startFormat = DateTimeFormat.forPattern("MMMM d - ");
        DateTimeFormatter endFormat = DateTimeFormat.forPattern("MMMM d, YYYY");

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
            this.emailService.sendGiftCard(p, reward, last.getGiftAmount());
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
            case WAIT:
                return "sessionHome/wait"; // check
            default:
                return "sessionHome/inProgress";
        }
    }

    @RequestMapping("/overview")
    public String overview(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        Study study  = p.getStudy();

        model.addAttribute("study", study);
        model.addAttribute("complete", p.getStudy().getCurrentSession().getName().equals("COMPLETE"));

        return "overview";
    }


    @RequestMapping("/next")
    public View nextStepInSession(ModelMap model, Principal principal) {

        Participant p = getParticipant(principal);
        Study study = p.getStudy();

        // Re-direct to the next step the current session is in progress.
        if(study.getState() == Study.STUDY_STATE.IN_PROGRESS) {
            return new RedirectView(study.getCurrentSession().getCurrentTask().getRequestMapping(), true);
        } else {
            emailService.sendSessionCompletedEmail(p);
            return new RedirectView("/session", true);
        }
    }

    @RequestMapping("/atRisk")
    public String atRisk(ModelMap model, Principal principal) {

        Participant p = getParticipant(principal);
        Study study = p.getStudy();
        model.addAttribute("study", study);
        return "atRisk";
    }

}


