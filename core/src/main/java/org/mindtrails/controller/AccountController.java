package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.forms.ParticipantForm;
import org.mindtrails.domain.forms.ParticipantUpdateForm;
import org.mindtrails.domain.recaptcha.RecaptchaFormValidator;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
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

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;

    @Autowired
    private RecaptchaFormValidator recaptchaFormValidator;

    @Autowired
    private ParticipantService participantService;

    /** This will assure that any form submissions for the participant Form
     * are validated for a proper recaptcha response.
     * @param binder
     */
    @InitBinder("participantForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(recaptchaFormValidator);
    }

    @RequestMapping(value="create", method = RequestMethod.GET)
    public String createForm (ModelMap model, Principal principal) {
        model.addAttribute("participantForm", new ParticipantForm());
        model.addAttribute("visiting", true);
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "account/create";
    }

    @RequestMapping(value="create", method = RequestMethod.POST)
    public String createNewParticipant(ModelMap model,
                                       @ModelAttribute("participantForm") @Valid ParticipantForm participantForm,
                                       final BindingResult bindingResult,
                                       HttpSession session
    ) {

        Participant participant;

        if(!participantForm.validParticipant(bindingResult, participantService)) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            model.addAttribute("visiting", true);
            model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
            return "account/create";
        }

        participant = participantService.create();
        participant.setFullName(participantForm.getFullName());
        participant.setEmail(participantForm.getEmail());
        participant.setAdmin(participantForm.isAdmin());

        participant.updatePassword(participantForm.getPassword());
        if(participantForm.getTheme()!=null)
            participant.setTheme(participantForm.getTheme());
        participant.setOver18(participantForm.isOver18());
        participant.setLastLoginDate(new Date());

        // Be sure to call saveNew rather than save, allowing
        // any data associated with the session to get
        // captured.
        participantService.saveNew(participant, session);

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participantForm.getEmail(), participantForm.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("Participant authenticated.");
        return "redirect:/account/theme";
    }

    @RequestMapping
    public String showAccount(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("participant", p);
        return "account";
    }

    @RequestMapping("theme")
    public String showTheme(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("participant", p);
        return "account/theme";
    }

    @RequestMapping(value="updateTheme", method = RequestMethod.POST)
    public String updateTheme(ModelMap model, String theme, Principal principal) {
        Participant p = participantService.get(principal);
        p.setTheme(theme);
        participantService.save(p);
        model.addAttribute("participant", p);
        return "redirect:/session/next";
    }

    @RequestMapping("exitStudy")
    public String exitStudy(ModelMap model, Principal principal) {
        Participant p      = participantService.get(principal);
        p.setActive(false);
        participantService.save(p);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping("debriefing")
    public String showDebriefing(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("participant", p);
        return "debriefing";
    }

    @RequestMapping(value="update", method = RequestMethod.POST)
    public String update(ModelMap model, Principal principal,
                         @Valid ParticipantUpdateForm form) {

            Participant p = participantService.get(principal);
            p.setEmail(form.getEmail());
            p.setFullName(form.getFullName());
            p.setEmailOptout(form.isEmailOptout());
            p.setTheme(form.getTheme());
            participantService.save(p);
            model.addAttribute("updated", true);
            model.addAttribute("participant", p);
        return "/account";
    }

    @RequestMapping("changePass")
    public String changePassword(ModelMap model, Principal principal) {
        model.addAttribute("participant", participantService.get(principal));
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

        participant =  participantService.get(principal);
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
        participantService.save(participant);
        participantService.flush();
        model.addAttribute("participant", participant);
        model.addAttribute("updated", true);
        return "account";
    }



}
