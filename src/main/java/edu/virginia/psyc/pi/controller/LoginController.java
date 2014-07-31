package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.PasswordToken;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private EmailService emailService;

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public LoginController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printWelcome(ModelMap model, Principal principal) {
        Authentication auth = (Authentication) principal;
        // Show the Rationale / Login page if the user is not logged in
        // otherwise redirect them to the session page.
        if(auth == null || !auth.isAuthenticated())
            return "rationale";
        else
            return "redirect:/session";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLogin() {
        return "rationale";
    }

    @RequestMapping(value="/loginfailed", method = RequestMethod.GET)
    public String loginerror(ModelMap model) {
        model.addAttribute("error", "true");
        return "/";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(ModelMap model) {
        return "/";
    }


    @RequestMapping("public/eligibility")
    public String showEligibility(ModelMap model) {
        // Template will set a difference form action if this variable is set to true.
        model.addAttribute("eligibility",true);
        return "questions/DASS21_AS";
    }

    @RequestMapping(value="public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("DASS21_AS") DASS21_AS dass21_as,
                                   ModelMap model) {

        if(dass21_as.eligibleScore()) {
            model.addAttribute("participant", new Participant());
            return "invitation";
        } else {
            return "ineligible";
        }
    }

    @RequestMapping("public/faq")
    public String showFaq(ModelMap model, Principal principal) {
        return "faq";
    }

    @RequestMapping("public/about")
    public String showAbout(ModelMap model, Principal principal) {
        return "about";
    }

    @RequestMapping("informed")
    public String showInformed(ModelMap model, Principal principal) {
        return "informed";
    }


    @RequestMapping("invitation")
    public String showInvitation(ModelMap model, Principal principal) {
        return "invitation";
    }

    @RequestMapping("public/privacy")
    public String showPrivacy(ModelMap model, Principal principal) {
        return "privacy";
    }

    @RequestMapping(value = "/newParticipant", method = RequestMethod.POST)
    public String createNewParticipant(ModelMap model,
                                       @Valid Participant participant,
                                       final BindingResult bindingResult,
                                       final SessionStatus status
                                       ) {


        model.addAttribute("participant", participant);

        if(participantRepository.findByEmail(participant.getEmail()).size() > 0) {
            bindingResult.addError(new ObjectError("email", "This email already exists."));
        }

        if(!participant.getPassword().equals(participant.getPasswordAgain())) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match."));
        }

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());

            return "invitation";
        } else {
            participant.setLastLoginDate(new Date()); // Set the last login date.
            saveParticipant(participant);
        }

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), participant.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("Participant authenticated.");
        return "redirect:/session";
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.GET)
    public String resetPass(ModelMap model) {
        return "resetPass";
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.POST)
    public String resetPass(ModelMap model, @RequestParam String email) throws MessagingException {

        Participant p;

        p = getParticipant(email);

        if(null == p) {
            model.addAttribute("invalidEmail", true);
            return("resetPass");
        }

        p.setPasswordToken(new PasswordToken());
        saveParticipant(p);

        emailService.sendPasswordReset(p);
        return("redirect:login");
    }

    @RequestMapping(value="/resetPassStep2/{token}", method = RequestMethod.GET)
    public String resetPassStep2(ModelMap model, @PathVariable String token) {

        Participant    participant;
        participant =  getParticipantByToken(token);

        if(participant != null) {
            model.addAttribute("participant", participant);
            model.addAttribute("token", token);
            return "changePassword";
        } else {
            model.addAttribute("invalidCode", true);
            model.addAttribute("participant", new Participant());
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
            model.addAttribute("participant", new Participant());
            return "login";
        }

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
        ParticipantDAO dao;
        Participant    participant = null;

        dao = participantRepository.findByToken(token);
        if(dao != null)
            participant = participantRepository.entityToDomain(dao);

        return participant;
    }


}