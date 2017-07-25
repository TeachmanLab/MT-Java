package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.*;
import edu.virginia.psyc.pi.domain.recaptcha.RecaptchaFormValidator;
import edu.virginia.psyc.pi.persistence.NotifyDAO;
import edu.virginia.psyc.pi.persistence.NotifyRepository;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_ASRepository;
import edu.virginia.psyc.pi.service.EmailService;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
public class LoginController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    // Eligibility form is saved to the session for retrieval when the user it found.
    private static final String DASS21_SESSION = "dass21";

    @Autowired
    private EmailService emailService;

    @Value("${tango.maxParticipants}")
    private long maxParticipantsForGiftCards;

    @Autowired
    private DASS21_ASRepository dass21_asRepository;

    @Autowired
    private NotifyRepository notifyRepository;

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;

    @Autowired
    private RecaptchaFormValidator recaptchaFormValidator;


    @InitBinder("participantCreateForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(recaptchaFormValidator);
    }


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public LoginController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printWelcome(ModelMap model, Principal principal, HttpSession session,
                               @RequestHeader(value = "referer", required = false) final String referer,
                               @RequestParam(value="cp", defaultValue = "unknown", required = false) String campaign) {

        session.setAttribute("reference", referer);
        session.setAttribute("campaign", campaign);
        model.addAttribute("campaign", campaign);
        Authentication auth = (Authentication) principal;
        // Show the Rationale / Login page if the user is not logged in
        // otherwise redirect them to the session page.
        if(auth == null || !auth.isAuthenticated()) {
            model.addAttribute("hideAccountBar", true);
            return "index";
        } else
            return "redirect:/session";
    }

    @RequestMapping(value="public/rationale", method = RequestMethod.GET)
    public String rationale(ModelMap model, Principal principal) {
         return "rationale";
    }

    @RequestMapping(value="public/notify", method = RequestMethod.POST)
    public String notify(@ModelAttribute("NotifyDAO") NotifyDAO notify,
                                   ModelMap model,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            return "rationale";
        }

        this.notifyRepository.save(notify);
        return "notify";
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
        Participant p = new Participant();
        p.setTheme("blue");
        model.addAttribute("participant",p);
        return "questions/DASS21_AS";
    }



    @RequestMapping(value="public/eligibilityCheck", method = RequestMethod.POST)
    public String checkEligibility(@ModelAttribute("DASS21_AS") DASS21_AS dass21_as,
                                   ModelMap model,
                                   HttpSession session) {

        // Save the DASS21_AS so we can track the number of failed attempts.  If the
        // user returns and tries to complete the session more than one time, record
        // this and save all attempts.
        dass21_as.setSessionId(session.getId());
        dass21_as.setDate(new Date());
        dass21_asRepository.save(dass21_as);
        model.addAttribute("participant", new Participant());

        // Redirect as appropriate
        if(dass21_as.eligibleScore()) {
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
            dass21.setSessionId(session.getId());
            dass21.setDate(new Date());
            dass21_asRepository.save(dass21);
            session.setAttribute("reference","PIMH");
            model.addAttribute("participant", new Participant());
            return "invitationPIMH";
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

    @RequestMapping("invitationPIMH")
    public String showInvitationPIMH(ModelMap model, Principal principal) {
        model.addAttribute("hideAccountBar", true);
        return "invitationPIMH";
    }

    @RequestMapping("public/privacy")
    public String showPrivacy(ModelMap model, Principal principal) {
        return "privacy";
    }

    @RequestMapping("public/disclaimer")
    public String showDisclaimer(ModelMap model, Principal principal) {
        return "disclaimer";
    }

    @RequestMapping(value="/consent", method = RequestMethod.GET)
    public String showConsent (ModelMap model, Principal principal) {
        model.addAttribute("participantCreateForm", new ParticipantCreateForm());
        model.addAttribute("hideAccountBar", true);
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "consent";
    }

    @RequestMapping(value = "/newParticipant", method = RequestMethod.POST)
    public String createNewParticipant(ModelMap model,
                                       @ModelAttribute("participantCreateForm") @Valid ParticipantCreateForm pForm,
                                       final BindingResult bindingResult,
                                       HttpSession session
                                       ) {

        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);

        if(!pForm.isOver18()) {
            bindingResult.addError(new ObjectError("over18", "You must be over 18 to participate in this Study."));
        }

        if(participantRepository.findByEmail(pForm.getEmail()) != null) {
            bindingResult.addError(new ObjectError("email", "This email already exists."));
        }

        if(!pForm.getPassword().equals(pForm.getPasswordAgain())) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match."));
        }

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            model.addAttribute("hideAccountBar", true);
            return "consent";
        }

        Participant participant = pForm.toParticipant();

        // Disable Gift Cards, if the max number is reached.
        long totalParticipants = participantRepository.count();
        participant.setReceiveGiftCards(maxParticipantsForGiftCards > totalParticipants);


        participant.setLastLoginDate(new Date()); // Set the last login date.
        participant.setReference((String)session.getAttribute("reference"));
        participant.setCampaign((String)session.getAttribute("campaign"));

        saveParticipant(participant);

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), participant.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Save the Eligibility form
        saveEligibilityForms(participant, session);

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
    private void saveEligibilityForms(Participant participant, HttpSession session) {
        ParticipantDAO   participantDAO = participantRepository.findByEmail(participant.getEmail());

        List<DASS21_AS> forms = dass21_asRepository.findBySessionId(session.getId());
        for(DASS21_AS dass21_as: forms) {
            dass21_as.setParticipantDAO(participantDAO);
            dass21_as.setSession(CBMStudy.NAME.ELIGIBLE.toString());
            dass21_asRepository.save(dass21_as);
        }
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
        model.addAttribute("hideAccountBar", true);

        if(participant != null) {
            model.addAttribute("participant", participant);
            model.addAttribute("token", token);
            return "/changePassword";
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
        if(!ParticipantCreateForm.validPassword(password)) {
            errors.add(ParticipantCreateForm.PASSWORD_MESSAGE);
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