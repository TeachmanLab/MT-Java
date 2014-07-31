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

import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private Participant getParticipant(Principal principal) {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()).get(0));
        return(p);
    }
    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public AccountController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping("/")
    public String showAccount(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "account";
    }

    @RequestMapping("exitStudy")
    public String exitStudy(ModelMap model, Principal principal) {
        Participant p      = getParticipant(principal);
        ParticipantDAO dao = participantRepository.findByEmail(principal.getName()).get(0);
        p.setActive(false);
        participantRepository.domainToEntity(p, dao);
        participantRepository.save(dao);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping("debriefing")
    public String showDebriefing(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "debriefing";
    }


}
