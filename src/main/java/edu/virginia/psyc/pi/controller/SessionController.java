package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.domain.Study;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
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


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public SessionController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @RequestMapping("")
    public String sessionHome(ModelMap model, Principal principal) {

        Participant p = getParticipant(principal);
        Study study  = p.getStudy();
        Session session = study.getCurrentSession();

        // Provide dates for next visit.
        DateTime startDate = new DateTime(p.getLastLoginDate()).plusDays(2);
        DateTime endDate = new DateTime(p.getLastLoginDate()).plusDays(5);
        DateTimeFormatter startFormat = DateTimeFormat.forPattern("MMMM d -");
        DateTimeFormatter endFormat = DateTimeFormat.forPattern("MMMM d, YYYY");

        model.addAttribute("participant", p);
        model.addAttribute("lastSession", study.getLastSession());
        model.addAttribute("currentSession", session);
        model.addAttribute("currentTask", session.getCurrentTask());
        model.addAttribute("sessionState", study.getState().toString());
        model.addAttribute("dateRange", startFormat.print(startDate) + endFormat.print(endDate));

        return "home";
    }

    @RequestMapping("/next")
    public String nextStepInSession(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        Study study = p.getStudy();
        study.completeCurrentTask();
        saveParticipant(p);
        return sessionHome(model, principal);
    }

}


