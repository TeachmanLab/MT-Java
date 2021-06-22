package org.mindtrails.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ReasonsForEnding;
import org.mindtrails.persistence.ReasonsForEndingRepository;
import org.mindtrails.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
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
    @Autowired private ExportService exportService;
    @Autowired private ImportService importService;
    @Autowired private ParticipantService participantService;
    @Autowired private ReasonsForEndingRepository reasonsForEndingRepository;

    @RequestMapping("")
    public String sessionHome(ModelMap model, Principal principal,
                              @RequestParam(value = "activate", required = false) boolean reactivate) throws Exception {

        Participant p = getParticipant(principal);
        Study study = p.getStudy();
        Session session = study.getCurrentSession();
        Session last = study.getLastSession();

        if(importService.isImporting()) {
            return "redirect:/";
        }

        if(reactivate) {
            p.setActive(true);
            participantService.save(p);
        }

        if(!p.isActive()) {
            List<ReasonsForEnding> surveys = reasonsForEndingRepository.findByParticipant(p);
            model.addAttribute("completed_exit_survey", surveys.size() > 0);
            return "sessionHome/inActive";
        }

        // Provide dates for next visit.
        DateTime startDate = new DateTime(study.getLastSessionDate()).plusDays(session.getDaysToWait());
        DateTime endDate = new DateTime(study.getLastSessionDate()).plusDays(session.getDaysToWait() + 2);
        DateTime completeBy = new DateTime(study.getLastSessionDate()).plusDays(session.getDaysToWait() + 2);
        DateTimeFormatter startFormat = DateTimeFormat.forPattern("YYYY-M-d - ");
        DateTimeFormatter endFormat = DateTimeFormat.forPattern("YYYY-M-d");

        model.addAttribute("lastSession", study.getLastSession());
        model.addAttribute("currentSession", session);
        model.addAttribute("currentTask", session.getCurrentTask());
        model.addAttribute("sessionState", study.getState().toString());
        model.addAttribute("dateRange", startFormat.print(startDate) + endFormat.print(endDate));
        model.addAttribute("completeBy", endFormat.print(completeBy));
        model.addAttribute("totalSessions", study.getSessions().size());
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

        // Determine if a gift should be awarded, and if so, create a record so we know to award it
        // in the admin after review.
        if(p.isReceiveGiftCards() && p.isVerified()) {
            model.addAttribute("nextGiftSession", study.nextGiftSession());
            if (last.getGiftAmount() > 0) {
                tangoService.prepareGift(p, session, last.getGiftAmount());
                model.addAttribute("giftAwarded", true);
                model.addAttribute("giftAmount", last.getGiftAmount());
            }
        }

        switch (study.getState()) {
            case IN_PROGRESS:
                return "sessionHome/inProgress";
            case ALL_DONE:
                return "sessionHome/allDone";
            case READY:
                return "sessionHome/ready";
            case WAIT:
                return "sessionHome/wait";
            default:
                return "sessionHome/inProgress";
        }
    }


    @RequestMapping("/overview")
    public String overview(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        Study study  = p.getStudy();

        model.addAttribute("study", study);
        model.addAttribute("giftcardsEnabled", study.isReceiveGiftCards());
        model.addAttribute("complete", p.getStudy().getCurrentSession().getName().equals("COMPLETE"));

        return "overview";
    }


    @ExportMode
    @RequestMapping("/next")
    public View nextStepInSession(ModelMap model, Principal principal) {

        Participant p = getParticipant(principal);
        Study study = p.getStudy();

        // Re-direct to the next step the current session is in progress.
        if(study.getState() == Study.STUDY_STATE.IN_PROGRESS && p.isActive()) {
            return new RedirectView(study.getCurrentSession().getCurrentTask().getRequestMapping(), true);
        } else {
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


