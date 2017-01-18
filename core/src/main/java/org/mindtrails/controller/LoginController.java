package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.mindtrails.domain.forms.ParticipantCreate;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides a default pathway into any html files that
 * don't require the user to be authenticated.
 * Files should be placed in resources/templates/
 * When rendering pages it sets a flag "visiting" to true
 * to indicate the current user is not logged in - so don't expect
 * to have a participant model available.
 *
 * The following html pages should exist to allow login:
 *
 * /resources/templates/login.html
 * /resources/templates/resetPass.html
 * /resources/templates/changePassword.html
 *
 *
 */
@Controller
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printWelcome(Principal principal) {
        Authentication auth = (Authentication) principal;
        // Show the Index / Login page if the user is not logged in
        // otherwise redirect them to the session page.
        if(auth == null || !auth.isAuthenticated())
            return "index";
        else
            return "redirect:/session";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLogin(ModelMap model) {
        return "login";
    }

    @RequestMapping(value="/public/{page}", method = RequestMethod.GET)
    public String showLogin(ModelMap model, @PathVariable("page") String page) {
        return page;
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.GET)
    public String resetPass(ModelMap model) {
        return "resetPass";
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.POST)
    public String resetPass(ModelMap model, @RequestParam String email) throws MessagingException {

        Participant p;

        p = participantService.findByEmail(email);

        if(null == p) {
            model.addAttribute("invalidEmail", true);
            return("resetPass");
        }

        p.setPasswordToken(new PasswordToken());
        participantService.save(p);

        emailService.sendPasswordReset(p);
        return("redirect:login");
    }

    @RequestMapping(value="/resetPassStep2/{token}", method = RequestMethod.GET)
    public String resetPassStep2(ModelMap model, @PathVariable String token) {

        Participant participant;
        participant =  getParticipantByToken(token);
        model.addAttribute("participant", participant);

        if(participant != null) {
            model.addAttribute("token", token);
            return "changePassword";
        } else {
            model.addAttribute("invalidCode", true);
            return "login";
        }
    }


    @RequestMapping(value="/changePassword", method = RequestMethod.POST)
    public String changePassword(ModelMap model, @RequestParam int id,
                                 @RequestParam String token,
                                 @RequestParam String password,
                                 @RequestParam String passwordAgain) throws MessagingException {

        Participant participant;
        List<String> errors;

        participant =  getParticipantByToken(token);

        errors      = new ArrayList<String>();

        // If the participant is null, then the token is invalid, send them back to the login page.
        if(null == participant || participant.getId() != id) {
            LOG.error("Change Password Page accessed with an invalid code, or the id's don't match");
            model.addAttribute("invalidCode", true);
            return "login";
        }

        if (!password.equals(passwordAgain)) {
            errors.add("Passwords do not match.");
        }
        if(!ParticipantCreate.validPassword(password)) {
            errors.add(ParticipantCreate.PASSWORD_MESSAGE);
        }

        if(errors.size() > 0) {
            model.addAttribute("errors", errors);
            model.addAttribute("token", token);
            model.addAttribute("participant", participant);
            return "changePassword";
        }

        participant.updatePassword(password); // save the password.
        participant.setPasswordToken(null);  // clear out hte token so it can't be used again.
        participant.setLastLoginDate(new Date()); // Set the last login date, as we will auto-login.
        participantService.save(participant);
        participantService.flush();
        // Log this person in, so they don't have to type the password again.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), participant.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOG.info("Participant authenticated.");
        return "redirect:/session";
    }

    /**
     * Returns a participant based on a Password token.
     * @param token
     * @return
     */
    Participant getParticipantByToken(String token) {
        return participantRepository.findByToken(token);
    }


}
