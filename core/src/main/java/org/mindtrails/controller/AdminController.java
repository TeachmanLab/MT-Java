package org.mindtrails.controller;

import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.mindtrails.domain.forms.ParticipantCreate;
import org.mindtrails.domain.forms.ParticipantCreateAdmin;
import org.mindtrails.domain.forms.ParticipantUpdateAdmin;
import org.mindtrails.domain.tango.Account;
import org.mindtrails.domain.tango.Order;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.ExportService;
import org.mindtrails.service.ParticipantService;
import org.mindtrails.service.TangoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private static final int PER_PAGE=20; // Number of users to display per page.

    @Autowired
    private EmailService emailService;

    @Autowired
    private TangoService tangoService;

    @Autowired
    private ExportService exportService;

    @Value("${export.disableDownloads}")
    private String downloadsDisabled;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    @ModelAttribute("visiting")
    public boolean visiting(Principal principal) {
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listParticipants(ModelMap model,Principal principal,
                            final @RequestParam(value = "search", required = false, defaultValue = "") String search,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        Page<Participant> daoList;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);
        pageRequest = new PageRequest(page, PER_PAGE);
        if(search.isEmpty()) {
            daoList = participantRepository.findAll(pageRequest);
        } else {
            daoList = participantRepository.search(search, pageRequest);
        }
        model.addAttribute("search", search);
        model.addAttribute("paging", daoList);
        model.addAttribute("participants", daoList);
        return "admin/participants";
    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String participantUpdateForm(ModelMap model,
                           @PathVariable("id") long id) {
        Participant p;
        ParticipantUpdateAdmin form;
        p    = participantRepository.findOne(id);
        form = new ParticipantUpdateAdmin(p);
        model.addAttribute("participantUpdateAdmin", form);
        model.addAttribute("participantEdit", p);
        return "admin/participantUpdate";
    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.POST)
    public String updateParticipant(ModelMap model,
                                       @PathVariable("id") long id,
                                       @Valid ParticipantUpdateAdmin form,
                                       BindingResult bindingResult) {
        Participant p;
        p = participantRepository.findOne(id);
        if(bindingResult.hasErrors()) {
            model.addAttribute("participantAdminForm", form);
            model.addAttribute("participantEdit", p);
            return "admin/participantUpdate";
        } else {
            form.updateParticipant(p);
            participantService.save(p);
            return "redirect:/admin";
        }
    }

    @RequestMapping(value="/participant/", method=RequestMethod.GET)
    public String participantCreateForm(ModelMap model) {

        ParticipantCreate form = new ParticipantCreate();

        model.addAttribute("participantCreateAdmin", form);
        return "admin/participantCreate";
    }

    @RequestMapping(value="/participant/", method=RequestMethod.POST)
    public String createParticipant(ModelMap model,
                                    @Valid ParticipantCreateAdmin participantCreateAdmin,
                                    BindingResult bindingResult,
                                    HttpSession session) {
        Participant participant;

        if(!participantCreateAdmin.validParticipant(bindingResult, participantService)) {
            model.addAttribute("participantCreateAdmin", participantCreateAdmin);
            return "admin/participantCreate";
        }

        participant = participantService.create();
        participant = participantCreateAdmin.updateParticipant(participant);
        participantService.save(participant);
        LOG.info("Participant created.");
        return "redirect:/admin";
    }


    @RequestMapping(value="/listEmails", method=RequestMethod.GET)
    public String listEmails(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("emails", emailService.emailTypes());
        return "admin/listEmails";
    }

    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") String type) throws Exception {
        Participant p = participantService.get(principal);

        if(type.equals(EmailService.TYPE.giftCard.toString())) {
            Reward reward = tangoService.createGiftCard(p, "test", 1);  // This would actually award a gift card, if you need to do some testing.
            this.emailService.sendGiftCard(p, reward, 100);
        } else if(type.equals(EmailService.TYPE.resetPass.toString())) {
            p.setPasswordToken(new PasswordToken());
            this.emailService.sendPasswordReset(p);
        } else if(type.equals(EmailService.TYPE.alertAdmin.toString())) {
            this.emailService.sendAdminEmail("Example", "This is an example alert message normally sent to the administrator with a custom subject and message");
        } else {
            Email email = emailService.getEmailForType(type);
            email.setParticipant(p);
            email.setTo(p.getEmail());
            email.setContext(new Context());
            emailService.sendExample(email);
        }
        return "redirect:/admin/listEmails";
    }

    @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("sessions", p.getStudy().getSessions());
        return "admin/listSessions";
    }

    // Trying to write a methods to get Tango Account information. By Diheng
    @RequestMapping(value="/tango",method = RequestMethod.GET)
    public String tangoInfo(ModelMap model, Principal principal){
        Account a = tangoService.getAccountInfo();
        model.addAttribute("tango",a);
        model.addAttribute("loggedIn", true);
        return "admin/tango";
    }

    @RequestMapping(value="/participant/giftCard")
    public String giftCard(ModelMap model, Principal principal) throws Exception {
        Participant p = participantService.get(principal);
        Reward r = tangoService.createGiftCard(p, "AdminAwarded",100);
        this.emailService.sendGiftCard(p, r, 100);
        return "/admin/participant_form";
    }

    @RequestMapping(value="/rewardInfo/{orderId}", method = RequestMethod.GET)
    public String showRewardInfo(ModelMap model, Principal principal, @PathVariable ("orderId") String orderId) {
        Order order = tangoService.getOrderInfo(orderId);
        model.addAttribute("order",order);
        return "admin/rewardInfo";
    }

}
