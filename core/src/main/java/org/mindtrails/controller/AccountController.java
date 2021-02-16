package org.mindtrails.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.MissingEligibilityException;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.domain.VerificationCode;
import org.mindtrails.domain.forms.ParticipantCreate;
import org.mindtrails.domain.forms.ParticipantUpdate;
import org.mindtrails.domain.recaptcha.RecaptchaFormValidator;
import org.mindtrails.service.ImportService;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.TangoService;
import org.mindtrails.service.TwilioService;
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
public class AccountController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;

    @Autowired
    private RecaptchaFormValidator recaptchaFormValidator;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TwilioService twilioService;

    @Autowired
    private TangoService tangoService;

    @Autowired
    private ImportService importService;

    /** This will assure that any form submissions for the participant Form
     * are validated for a proper recaptcha response.
     * @param binder
     */
    @InitBinder("participantForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(recaptchaFormValidator);
    }


    private  void addAttributesForCreateParticipantForm(ModelMap model) {
        if (importService.isImporting()) {
            model.addAttribute("enableVerification",false);
        } else {
            model.addAttribute("enableVerification",true);
        }
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        model.addAttribute("giftcardsEnabled", tangoService.getEnabled());
    }

    @RequestMapping(value="create", method = RequestMethod.GET)
    public String createForm (ModelMap model, HttpSession session) {

        addAttributesForCreateParticipantForm(model);
        model.addAttribute("participantForm", new ParticipantCreate());
        if(participantService.isEligible(session) || importService.isImporting()) {
            return "account/create";
        } else {
            return "redirect:/public/eligibility";
        }
    }

    @RequestMapping(value="create", method = RequestMethod.POST )
    public String createNewParticipant(ModelMap model,
                                       @ModelAttribute("participantForm") @Valid ParticipantCreate participantCreate,
                                       final BindingResult bindingResult,
                                       HttpSession session
    ) {

        Participant participant;

        if(!participantCreate.validParticipant(bindingResult, participantService)) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            addAttributesForCreateParticipantForm(model);
            return ("account/create");
        }

        participant = participantService.create();
        participantCreate.updateParticipant(participant);
        participant.setLastLoginDate(new Date());
        participant.setVerificationCode(new VerificationCode(participant));
        participant.setReference((String)session.getAttribute("referer"));
        participant.setCampaign((String)session.getAttribute("campaign"));

        // Be sure to call saveNew rather than save, allowing
        // any data associated with the session to get
        // captured.  If this is in the importService mode, then we need to allow
        // the creation of a new account /without/ capturing any additional eligibility
        // requirements.
        if(this.importService.isImporting()) {
            participantService.save(participant);
        } else {
            try {
                participantService.saveNew(participant, session);
            } catch (MissingEligibilityException mee) {
                return "redirect:/public/eligibility";
            }
        }

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participantCreate.getEmail(), participantCreate.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (participant.isReceiveGiftCards()){
            String code=participant.getVerificationCode().getCode();
            TextMessage message = new TextMessage("verificationCode", code);
            twilioService.sendMessage(message,participant);
            return "redirect:/account/verification";
        }

        // This is a bit of hack for a specific study, just trying to hold this together and get it
        // out he door quickly.
        if (participant.getStudy().getConditioning().equals("CAN_COACH")) {
            return "redirect:/account/coachingOptIn";
        }

        return "redirect:/account/theme";
    }

    @RequestMapping("coachingOptIn")
    public String showCoachingOptIn(ModelMap model, Principal principal) {
        return "account/coachingOptIn";
    }

    @ExportMode
    @RequestMapping(value="setCoachingPreference", method = RequestMethod.POST)
    public String setCoachingPreference(ModelMap model, Boolean wantsCoaching, String firstCoachingFormat, Principal principal) {
        Participant p = participantService.get(principal);
        p.setWantsCoaching(wantsCoaching);
        p.setFirstCoachingFormat(firstCoachingFormat);
        participantService.save(p);
        return "redirect:/account/theme";
    }


    @RequestMapping("theme")
    public String showTheme(ModelMap model, Principal principal) {
        return "account/theme";
    }

    @ExportMode
    @RequestMapping(value="updateTheme", method = RequestMethod.POST)
    public String updateTheme(ModelMap model, String theme, Principal principal) {
        Participant p = participantService.get(principal);
        p.setTheme(theme);
        participantService.save(p);
        return "redirect:/session";
    }

//when a duplicated phone number is detected
    @RequestMapping("changePhone")
    public String showPhone(ModelMap model, Principal principal) {
        Participant p=participantService.get(principal);
        if(p.isVerified()){
            return "redirect:/account/verified";
        }
        return "account/changePhone";
    }

    private void sendNewVerificationCode(Participant p) {
        p.setVerificationCode(new VerificationCode(p));
        participantService.save(p);
        String code=p.getVerificationCode().getCode();
        TextMessage message = new TextMessage("verificationCode", code);
        twilioService.sendMessage(message,p);
    }

    @ExportMode
    @RequestMapping(value="updateChangePhone",method=RequestMethod.POST)
    public String updatePhone( ModelMap model, Principal principal, String phone) {
        Participant p=participantService.get(principal);
            if(p.getPhone().equals(phone) || participantService.findByPhone(formatPhone(phone)).isEmpty()){
                p.updatePhone(formatPhone(phone));
                sendNewVerificationCode(p);
                return "redirect:/account/verification";
            }
            else{
                return "redirect:/account/changePhone";

            }
    }




//when the user enter a wrong or invalid (>1h) verification code
    @RequestMapping("wrongCode")
    public String showwrongcode(ModelMap model, Principal principal) {
        //ParticipantUpdate update = new ParticipantUpdate();
        //update.fromParticipant(getParticipant(principal));
        //model.addAttribute("participantUpdate", update);
        Participant p=participantService.get(principal);
        if(p.isVerified()){
            return "redirect:/account/verified";
        }
        return "account/wrongCode";

    }

    public String formatPhone(String p) {
        String phoneLocale="US";
        if(p == null) return null;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phone = phoneUtil.parse(p, phoneLocale);
            return phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return p; // Leave it alone, let validation handle it.
        }
    }

    @ExportMode
    @RequestMapping(value="updateWrongCode",method=RequestMethod.POST)
    public String updatewrongcode(@RequestParam String sub, @RequestParam String verifycode, ModelMap model, Principal principal, String phone) {
        Participant p=participantService.get(principal);
        if( sub.equals("subCode") ){
            if (p.getVerificationCode().getCode().equals(verifycode)&&p.getVerificationCode().valid()) {
                p.setVerified(true);
                p.setReceiveGiftCards(true);
                //p.updateGiftCardsQualification();
                participantService.save(p);
                return "redirect:/account/verified";

            }  else {
                return "redirect:/account/wrongCode";
            }

        }
        else if( sub.equals("subPhone") ){

           if(participantService.findByPhone(formatPhone(phone)).isEmpty()||formatPhone(phone).equals(p.getPhone())){
                p.updatePhone(formatPhone(phone));
                sendNewVerificationCode(p);
                return "redirect:/account/verification";
            }
            else{
               model.addAttribute("invalidPhone", true);
               return "account/wrongCode";
           }
        }
        else {

            return "redirect:/account/wrongCode";
        }

    }

  //when an account is verified
    @RequestMapping(value="/verified",method= RequestMethod.GET)
    public String verified(){
        return "account/verified";
    }

    //when an account is verified
    @RequestMapping(value="/tango",method= RequestMethod.POST)
    public String setGiftCard(){
        return "account/tango";
    }


    //when a user want to do the phone verification during the study
    //it is a link under "My Account"
    @RequestMapping("PostVerification")
    public String PostVerification(@RequestParam(value="verifycode", required=false, defaultValue="NAN") String verifycode,ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        sendNewVerificationCode(p);
        return "redirect:/account/verification";
    }


    //verfication page
    @RequestMapping("verification")
    public String verify(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        if (p.isVerified()) {
            return "redirect:/account/verified";
//        } else if (participantService.findByPhone(formatPhone(p.getPhone())).size()>1) {
//           return "redirect:/account/changePhone";
        } else {
            return "account/verification";

        }
    }

    @ExportMode
    @RequestMapping(value="/updateVerification", method = RequestMethod.POST)
    public String verify(@RequestParam String verifycode, ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);

        String code=p.getVerificationCode().getCode();
        if(p.isVerified()){
            return "redirect:/account/verified";
        }
         if (code.equals(verifycode)&&p.getVerificationCode().valid()) {
             p.setVerified(true);
             p.setReceiveGiftCards(true);
             participantService.save(p);
             return "redirect:/account/verified";
         }
         else {
             return "redirect:/account/wrongCode";
         }
    }

    @ExportMode
    @RequestMapping(value="updateCardCountry",method=RequestMethod.POST)
    public String updateCardCountry( ModelMap model, Principal principal, @RequestParam String country) {
        Participant p=participantService.get(principal);
        p.setAwardCountryCode(country);
        participantService.save(p);
        return "redirect:/session/next";
    }



    @RequestMapping("exitStudyConfirm")
    public String exitStudyConfirm(ModelMap model, Principal principal) {
        return "exitStudyConfirm";
    }

    @RequestMapping("exitStudy")
    public String exitStudy(ModelMap model, Principal principal) {
        Participant p      = participantService.get(principal);
        p.setActive(false);
        participantService.save(p);
        model.addAttribute("isExiting", true);
        return "debriefing";
    }

    @RequestMapping("debriefing")
    public String showDebriefing(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("isExiting", false);
        return "debriefing";
    }

    @RequestMapping
    public String showAccount(ModelMap model, Principal principal) {
        ParticipantUpdate update = new ParticipantUpdate();
        update.fromParticipant(getParticipant(principal));
        boolean verified=participantService.get(principal).isVerified();
        model.addAttribute("participantUpdate", update);
        model.addAttribute("verified", verified);
        model.addAttribute("postChange", true);
        return "account";
    }

    @ExportMode
    @RequestMapping(value="update", method = RequestMethod.POST)
    public String update(ModelMap model, Principal principal,
                         @Valid ParticipantUpdate form,
                         BindingResult bindingResult) {

        Participant participant = getParticipant(principal);

        if(bindingResult.hasErrors()) {
            model.addAttribute("participantUpdate", form);
            return "account";
        } else {
            Participant p = participantService.get(principal);
            form.updateParticipant(p);
            participantService.save(p);
            model.addAttribute("updated", true);
            return "redirect:/session";
        }
    }

    @RequestMapping("changePass")
    public String changePassword(ModelMap model, Principal principal) {
        return "changePassword";
    }

    @ExportMode
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

        if(!ParticipantCreate.validPassword(password)) {
            errors.add(ParticipantCreate.PASSWORD_MESSAGE);
        }

        if(errors.size() > 0) {
            model.addAttribute("errors", errors);
            model.addAttribute("token", token);
            return "changePassword";
        }

        participant.updatePassword(password); // save the password.
        participant.setPasswordToken(null);  // clear out hte token so it can't be used again.
        participant.setLastLoginDate(new Date()); // Set the last login date, as we will auto-login.
        participantService.save(participant);
        participantService.flush();
        ParticipantUpdate update = new ParticipantUpdate();
        update.fromParticipant(participant);
        model.addAttribute("updated", true);
        model.addAttribute("participantUpdate", update);
        return "account";
    }



}
