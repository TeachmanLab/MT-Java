package edu.virginia.psyc.pi.mvc;

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
public class SessionController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

    private Participant getParticipant(Principal principal) {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()).get(0));
        return(p);
    }

    private void saveParticipant(Participant participant) {
        ParticipantDAO dao;

        if(participant.getId() > 0) {
            dao = participantRepository.findOne(participant.getId());
        } else {
            dao = new ParticipantDAO();
        }

        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);
    }

    @RequestMapping("")
    public String sessionHome(ModelMap model, Principal principal) {

        Participant p = getParticipant(principal);

        model.addAttribute("name", p.getFullName());
        model.addAttribute("participant", p);
        model.addAttribute("currentSession", p.getCurrentSession());
        model.addAttribute("currentTask", p.getCurrentSession().getCurrentTask());

        return "home";
    }

    @RequestMapping("/next")
    public String nextStepInSession(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        p.setTaskIndex(p.getTaskIndex() + 1);
        saveParticipant(p);
        return sessionHome(model, principal);
    }

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public SessionController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    /** Pre-Assessment
     Pre-Treatment
     •	DASS-21-AS (if more than 2 days after eligibility screening)
     •	Credibility Scale
     •	Mental Health History
     •	State Anxiety Assessment
     •	Interpretation bias measures (Lexical Decision Task & Recognition Ratings)
     •	Quality of Life Scale
     •	Comorbid symptom measures (DASS-21-depression, AUDIT)
     * ---------**/
    @RequestMapping(value="pre", method=RequestMethod.GET)
    public String pre(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        return "/questions/demographics";
    }

    /** Week 1
     * ---------**/
    @RequestMapping(value="week1", method=RequestMethod.GET)
    public String week1() {
        return "/questions/demographics";
    }

    /** Week 2
     * ---------**/
    @RequestMapping(value="week2", method=RequestMethod.GET)
    public String week2() {
        return "/questions/demographics";
    }

    /** Week 3
     * ---------**/
    @RequestMapping(value="week3", method=RequestMethod.GET)
    public String week3() {
        return "/questions/demographics";
    }

    /** Week 4
     * ---------**/
    @RequestMapping(value="week4", method=RequestMethod.GET)
    public String week4() {
        return "/questions/demographics";
    }

    /** Post-Assessment
     * ---------**/
    @RequestMapping(value="post", method=RequestMethod.GET)
    public String post() {
        return "/questions/demographics";
    }



}


