package edu.virginia.psyc.mindtrails.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.forms.ParticipantForm;
import edu.virginia.psyc.mindtrails.domain.forms.ParticipantUpdateForm;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.X
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant getParticipant(Principal p) {
        return participantRepository.findByEmail(p.getName());
    }

    @RequestMapping
    public String showAccount(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "account";
    }

    @RequestMapping("theme")
    public String showTheme(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "theme";
    }

    @RequestMapping(value="updateTheme", method = RequestMethod.POST)
    public String updateTheme(ModelMap model, String theme, Principal principal) {
        Participant p = getParticipant(principal);
        p.setTheme(theme);
        participantRepository.save(p);
        model.addAttribute("participant", p);
        return "redirect:/session/next";
    }

    @RequestMapping("exitStudy")
    public String exitStudy(ModelMap model, Principal principal) {
        Participant p      = getParticipant(principal);
        p.setActive(false);
        participantRepository.save(p);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping("debriefing")
    public String showDebriefing(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping(value="update", method = RequestMethod.POST)
    public String update(ModelMap model, Principal principal,
                         @Valid ParticipantUpdateForm form) {

            Participant p = getParticipant(principal);
            p.setEmail(form.getEmail());
            p.setFullName(form.getFullName());
            p.setEmailOptout(form.isEmailOptout());
            p.setTheme(form.getTheme());
            participantRepository.save(p);
            model.addAttribute("updated", true);
            model.addAttribute("participant", p);
        return "/account";
    }

    @RequestMapping("changePass")
    public String changePassword(ModelMap model, Principal principal) {
        model.addAttribute("participant", getParticipant(principal));
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
        if(!ParticipantForm.validPassword(password)) {
            errors.add(ParticipantForm.PASSWORD_MESSAGE);
        }

        if(errors.size() > 0) {
            model.addAttribute("errors", errors);
            model.addAttribute("participant", participant);
            model.addAttribute("token", token);
            return "changePassword";
        }

        participant.updatePassword(password); // save the password.
        participant.setPasswordToken(null);  // clear out hte token so it can't be used again.
        participant.setLastLoginDate(new Date()); // Set the last login date, as we will auto-login.
        participantRepository.save(participant);
        participantRepository.flush();
        model.addAttribute("participant", participant);
        model.addAttribute("updated", true);
        return "account";
    }



}
