package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
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

        model.addAttribute("name", p.getFullName());
        model.addAttribute("participant", p);
        model.addAttribute("currentSession", p.getCurrentSession());
        model.addAttribute("currentTask", p.getCurrentSession().getCurrentTask());
        model.addAttribute("sessionState", p.sessionState().toString());

        return "home";
    }

    @RequestMapping("/next")
    public String nextStepInSession(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        p.completeCurrentTask();
        saveParticipant(p);
        return sessionHome(model, principal);
    }

}


