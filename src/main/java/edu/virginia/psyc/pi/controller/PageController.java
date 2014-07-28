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
@RequestMapping("/pages")
public class PageController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(PageController.class);

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
    public PageController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping("faq")
    public String showFaq(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "faq";
    }

    @RequestMapping("account")
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

    @RequestMapping("email")
    public String showEmail(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "tempEmail";
    }

    @RequestMapping("about")
    public String showAbout(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "about";
    }

    @RequestMapping("informed")
    public String showInformed(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "informed";
    }

    @RequestMapping("debriefing")
    public String showDebriefing(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping("rationale")
    public String showRationale(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "rationale";
    }

    @RequestMapping("invitation")
    public String showInvitation(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "invitation";
    }

    @RequestMapping("privacy")
    public String showPrivacy(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "privacy";
    }



}
