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
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public AccountController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping
    public String showAccount(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "account";
    }

    @RequestMapping("exitStudy")
    public String exitStudy(ModelMap model, Principal principal) {
        Participant p      = getParticipant(principal);
        ParticipantDAO dao = participantRepository.findByEmail(principal.getName());
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

    @RequestMapping("changePass")
    public String changePassword(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "changePassword";
    }

    @RequestMapping(value="/changePassword", method = RequestMethod.POST)
    public String changePassword(ModelMap model, Principal principal,
                                 @RequestParam int id,
                                 @RequestParam String token,
                                 @RequestParam String password,
                                 @RequestParam String passwordAgain) throws MessagingException {

        Participant participant;
        List<String> errors;

        participant =  getParticipant(principal);
        errors      = new ArrayList<String>();

        if (!password.equals(passwordAgain)) {
            errors.add("Passwords do not match.");
        }
        if(!Participant.validPassword(password)) {
            errors.add(Participant.PASSWORD_MESSAGE);
        }

        if(errors.size() > 0) {
            model.addAttribute("errors", errors);
            model.addAttribute("participant", participant);
            model.addAttribute("token", token);
            return "changePassword";
        }

        participant.setPassword(password); // save the password.
        participant.setPasswordToken(null);  // clear out hte token so it can't be used again.
        participant.setLastLoginDate(new Date()); // Set the last login date, as we will auto-login.
        saveParticipant(participant);
        participantRepository.flush();
        model.addAttribute("participant", participant);
        model.addAttribute("updated", true);
        return "account";
    }



}
