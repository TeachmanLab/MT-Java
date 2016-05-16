package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.PasswordToken;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Dass21FromPi;
import edu.virginia.psyc.pi.domain.ParticipantForm;
import edu.virginia.psyc.pi.domain.PiParticipant;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_ASRepository;
import edu.virginia.psyc.pi.service.PiEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    // Eligibility form is saved to the session for retrieval when the user it found.
    private static final String DASS21_SESSION = "dass21";

    @Autowired
    private PiEmailService emailService;

    @Autowired
    private DASS21_ASRepository dass21_asRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant getParticipant(Principal p) {
        return participantRepository.findByEmail(p.getName());
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printWelcome(Principal principal) {
        Authentication auth = (Authentication) principal;
        // Show the Rationale / Login page if the user is not logged in
        // otherwise redirect them to the session page.
        if(auth == null || !auth.isAuthenticated())
            return "rationale";
        else
            return "redirect:/session";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLogin(ModelMap model) {
        model.addAttribute("hideAccountBar", true);
        return "login";
    }

    // Added by Diheng, linking to login page
   // @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
   // public String goLoginPage(){
   //     return "loginPage";
   // }

    @RequestMapping(value="/loginfailed", method = RequestMethod.GET)
    public String loginerror(ModelMap model) {
        model.addAttribute("error", "true");
        model.addAttribute("hideAccountBar", true);
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
        PiParticipant p = new PiParticipant();
        p.setTheme("blue");
        model.addAttribute("participant",p);

        return "questions/DASS21_AS";
    }



    @RequestMapping(value="public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("DASS21_AS") DASS21_AS dass21_as,
                                   ModelMap model,
                                   HttpSession session) {

        if(dass21_as.eligibleScore()) {
            // Save the DASS21_AS object in the session, so we can grab it when the
            // user is logged in.
            session.setAttribute("dass21", dass21_as);
            model.addAttribute("participant", new PiParticipant());
            return "invitation";
        } else {
            return "ineligible";
        }
    }

    // An external endpoint that bypasses the eligibility form, in the case
    // where the form is filled out on a remote site, and it's results are
    // added here.
    @RequestMapping(value="public/eligible", method = RequestMethod.POST)
    public String eligable(@ModelAttribute Dass21FromPi data,
                                   ModelMap model,
                                   HttpSession session) throws Exception {
        model.addAttribute("hideAccountBar", true);
        DASS21_AS dass21 = data.asDass21Object();
        if(dass21.eligibleScore()) {
            // Save the DASS21_AS object in the session, so we can grab it when the
            // user is logged in.
            session.setAttribute("dass21", dass21);
            model.addAttribute("participant", new PiParticipant());
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

    @RequestMapping("public/team")
    public String showTeam(ModelMap model, Principal principal) {
        return "team";
    }

    @RequestMapping("public/contact")
    public String showoContact(ModelMap model, Principal principal) {
        return "contact";
    }

    @RequestMapping("informed")
    public String showInformed(ModelMap model, Principal principal) {
        return "informed";
    }

    @RequestMapping("public/researchSupport")
    public String showResearchSupport(ModelMap model, Principal principal) {
        return "researchSupport";
    }


    @RequestMapping("invitation")
    public String showInvitation(ModelMap model, Principal principal) {
        model.addAttribute("hideAccountBar", true);
        return "invitation";
    }

    @RequestMapping(value="/consent", method = RequestMethod.GET)
    public String showConsent (ModelMap model, Principal principal) {
        model.addAttribute("participant", new PiParticipant());
        model.addAttribute("hideAccountBar", true);
        return "consent";

    }

/**
 *
 * Privacy Policy
 *
 * */
    @RequestMapping("public/privacy")
    public String showPrivacy(ModelMap model, Principal principal) {
        return "privacy";
    }


    /**
     *  Disclaimer
     *
     */

    @RequestMapping("public/disclaimer")
    public String showDisclaimer(ModelMap model, Principal principal) {
        return "disclaimer";
    }

    @RequestMapping(value = "/newParticipant", method = RequestMethod.POST)
    public String createNewParticipant(ModelMap model,
                                       @Valid ParticipantForm participantForm,
                                       final BindingResult bindingResult,
                                       final SessionStatus status,
                                       HttpSession session
                                       ) {

        Participant participant;
        model.addAttribute("participant", participantForm);

        if(!participantForm.validParticipant(bindingResult, participantRepository)) {
            model.addAttribute("hideAccountBar", true);
            return "consent";
        }

        participant = participantForm.toPiParticipant();
        participantRepository.save(participant);

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), participant.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Save the Eligibility form
        saveEligibilityForm(participant, session);

        LOG.info("Participant authenticated.");
        return "redirect:/account/theme";
    }

    /**
     * Users have to complete an eligibility form to create an account, but you can't
     * save the form till the account exists.  So the results of the form are stored
     * in the session until the user creates an account, at which point
     * they are recorded to the database.
     * @param participant
     * @param session
     */
    private void saveEligibilityForm(Participant participant, HttpSession session) {
        DASS21_AS dass21_as;

        // Save their dass21 score to the Database
        dass21_as = (DASS21_AS)session.getAttribute(DASS21_SESSION);
        if(dass21_as == null) return;   // No eligiblity form exists in the session.
        dass21_as.setParticipant(participant);
        dass21_as.setDate(new Date());
        dass21_as.setSession(CBMStudy.NAME.ELIGIBLE.toString());
        dass21_asRepository.save(dass21_as);
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.GET)
    public String resetPass(ModelMap model) {
        return "resetPass";
    }

    @RequestMapping(value="/resetPass", method = RequestMethod.POST)
    public String resetPass(ModelMap model, @RequestParam String email) throws MessagingException {

        Participant p;

        p = participantRepository.findByEmail(email);

        if(null == p) {
            model.addAttribute("invalidEmail", true);
            return("resetPass");
        }

        p.setPasswordToken(new PasswordToken());
        participantRepository.save(p);

        emailService.sendPasswordReset(p);
        return("redirect:login");
    }

    @RequestMapping(value="/resetPassStep2/{token}", method = RequestMethod.GET)
    public String resetPassStep2(ModelMap model, @PathVariable String token) {

        Participant participant;
        participant =  getParticipantByToken(token);
        model.addAttribute("hideAccountBar", true);

        if(participant != null) {
            model.addAttribute("participant", participant);
            model.addAttribute("token", token);
            return "/changePassword";
        } else {
            model.addAttribute("invalidCode", true);
            model.addAttribute("participant", new PiParticipant());
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
            model.addAttribute("participant", new PiParticipant());
            return "login";
        }

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