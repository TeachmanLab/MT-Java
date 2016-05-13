package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.domain.ParticipantForm;
import edu.virginia.psyc.pi.domain.ParticipantListForm;
import edu.virginia.psyc.pi.domain.PiParticipant;
import edu.virginia.psyc.mindtrails.domain.tango.Account;
import edu.virginia.psyc.mindtrails.domain.tango.Order;
import edu.virginia.psyc.mindtrails.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.PiParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.OA;
import edu.virginia.psyc.pi.persistence.TrialRepository;
import edu.virginia.psyc.pi.service.PiEmailService;
import edu.virginia.psyc.mindtrails.service.ExportService;
import edu.virginia.psyc.mindtrails.service.TangoService;
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

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
    private PiEmailService emailService;

    @Autowired
    private TrialRepository trialRepository;

    @Autowired
    private PiParticipantRepository piParticipantRepository;

    @Autowired
    private TangoService tangoService;

    @Autowired
    private ExportService exportService;

    @Value("${export.disableDownloads}")
    private String downloadsDisabled;




    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public AdminController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listUsers(ModelMap model,Principal principal,
                            final @RequestParam(value = "search", required = false, defaultValue = "") String search,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        Participant p = getParticipant(principal);

        ParticipantListForm form;
        Page<Participant> daoList;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);

        pageRequest = new PageRequest(page, PER_PAGE);

        if(search.isEmpty()) {
            daoList = participantRepository.findAll(pageRequest);
        } else {
            daoList = participantRepository.search(search, pageRequest);
        }

        form = new ParticipantListForm();
        for(Participant participant : daoList) {
            form.add(participant.getStudy().getCurrentSession().getName());
        }

        model.addAttribute("hideAccountBar", true);
        model.addAttribute("participantForm", form);
        model.addAttribute("search", search);
        model.addAttribute("paging", daoList);
        return "admin/admin";

    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String showForm(ModelMap model,
                           @PathVariable("id") long id) {
        Participant p;
        p = participantRepository.findOne(id);

        model.addAttribute("hideAccountBar", true);
        model.addAttribute("participant", p);
        return "admin/participant_form";
    }

    @RequestMapping(value="/updateParticipants", method=RequestMethod.POST)
    public String updateParticipants(ModelMap model,
                                     @ModelAttribute("participants") ParticipantListForm participantForm) {

        List<PiParticipant> participants = participantForm.getParticipants();
        List<String> sessions = participantForm.getSessionNames();
        int     index;
        PiParticipant participant;

        // We only want to update a very limited set of fields on the participant
        // data model.
        if(null != participants && participants.size() > 0) {
            for (PiParticipant p : participants) {
                index = participants.indexOf(p);
                participant = piParticipantRepository.findOne(p.getId());
                participant.setActive(p.isActive());
                participant.setPrime(p.getPrime());
                participant.setCbmCondition(p.getCbmCondition());
                // Only if the session was change in the ui, update the session
                // current session for the participant, and reset their progress.
                // set the last session date to null so they don't get a timeout
                // message.
                if(p.getStudy().getCurrentSession().getName() != sessions.get(index)) {
                    p.getStudy().forceToSession(sessions.get(index));
                }
                participantRepository.save(participant);
            }
        }
        return "redirect:/admin";
    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.POST)
    public String checkParticipantInfo(ModelMap model,
                                       @PathVariable("id") long id,
                                       @Valid PiParticipant participant,
                                       BindingResult bindingResult) {
        Participant dao;

        dao = participantRepository.findOne(id);

//        if (bindingResult.hasErrors()) {
//            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
//            model.addAttribute("participant", participant);
//            return "admin/participant_form";
 //       } else {
            participantRepository.save(dao);
 //       }
        return "redirect:/admin";
    }

    @RequestMapping(value="/new_participant", method=RequestMethod.GET)
    public String showNewForm(ModelMap model) {
        PiParticipant p;
        p = new PiParticipant();
        model.addAttribute("hideAccountBar", true);
        model.addAttribute("participant", p);
        return "admin/new_participant";
    }


    @RequestMapping(value="/participant/", method=RequestMethod.POST)
    public String createParticipant(ModelMap model,
                                    @Valid ParticipantForm form,
                                    BindingResult bindingResult) {

        Participant participant;
        model.addAttribute("hideAccountBar", true);
        form.setOver18(true);

        if(!form.validParticipant(bindingResult, participantRepository)) {
            return "admin/new_participant";
        }

        participant = form.toPiParticipant();
        saveParticipant(participant);

        LOG.info("Participant created.");
        return "redirect:/admin";
    }


    @RequestMapping(value="/listEmails", method=RequestMethod.GET)
    public String listEmails(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("hideAccountBar", true);
        model.addAttribute("participant", p);
        return "admin/listEmails";
    }

    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") PiEmailService.TYPE type) throws Exception {
        Participant p = participantRepository.findByEmail(principal.getName());

        if(type.equals(PiEmailService.TYPE.giftCard)) {
            // Reward reward = tangoService.createGiftCard(p);  This would actually award a gift card, if you need to do some testing.
            Reward reward = new Reward("111-111-111-111", "1234-2345-2345", "123", "https://www.google.com", "12345");
            this.emailService.sendGiftCardEmail(p, reward, 100);
        } else if(type.equals(PiEmailService.TYPE.alertAdmin)) {
            OA d1 = new OA(1,2,3,4,5);
            OA d2 = new OA(3,3,4,5,5);
            this.emailService.sendAtRiskAdminEmail(p, d1, d2);
        } else {
            this.emailService.sendSimpleMail(p, type);
        }

        return "redirect:/admin/listEmails";
    }

    @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getStudy().getSessions());
        model.addAttribute("hideAccountBar", true);
        return "admin/listSessions";
    }

    @RequestMapping(value="/export", method=RequestMethod.GET)
    public String export(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);

        model.addAttribute("downloadsDisabled", Boolean.parseBoolean(downloadsDisabled));
        model.addAttribute("exportMaxMinutes", exportService.getMaxMinutes());
        model.addAttribute("exportMaxRecords", exportService.getMaxRecords());
        model.addAttribute("totalRecords", exportService.totalDeleteableRecords());
        model.addAttribute("minutesSinceLastExport", exportService.minutesSinceLastExport());
        model.addAttribute("formsDisabled", exportService.disableAdditionalFormSubmissions());

        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getStudy().getSessions());
        model.addAttribute("hideAccountBar", true);
        return "admin/export";
    }

    // Trying to write a methods to get Tango Account information. By Diheng

    @RequestMapping(value="/checkFunds",method = RequestMethod.GET)
    public String checkFunds(ModelMap model, Principal principal){
        Account a = tangoService.getAccountInfo();
        model.addAttribute("tango",a);
        model.addAttribute("hideAccountBar", true);
        return "admin/checkFunds";
    }

    // Added by Diheng, try to send gift card to participants;

    @RequestMapping(value="/participant/giftCard")
    public String giftCard(ModelMap model, Principal principal) throws Exception {
        Participant p = participantRepository.findByEmail(principal.getName());
        Reward r = tangoService.createGiftCard(p, "AdminAwarded",100);
        this.emailService.sendGiftCardEmail(p, r, 100);
        model.addAttribute("participant", p);
        model.addAttribute("hideAccountBar", true);
        return "/admin/participant_form";
    }


    // Added by Diheng, try to recall reward information by order ID;
    @RequestMapping(value="/rewardInfo/{orderId}", method = RequestMethod.GET)
    public String showRewardInfo(ModelMap model, Principal principal, @PathVariable ("orderId") String orderId) {
        Order order = tangoService.getOrderInfo(orderId);
        model.addAttribute("order",order);
        model.addAttribute("hideAccountBar", true);
        return "admin/rewardInfo";
    }


//    @RequestMapping(value="/rewardInfo/{orderId}", method=RequestMethod.POST)
//    public String checkRewardInfo(ModelMap model,
//                                       @PathVariable("orderId") String orderId,
//                                       @Valid Participant participant,
//                                       BindingResult bindingResult) {
//        GiftLogDAO dao;

//        dao = ;

//        if (bindingResult.hasErrors()) {
//            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
//            model.addAttribute("participant", participant);
//            return "admin/participant_form";
        //       } else {
//        participantRepository.domainToEntity(participant, dao);
//        participantRepository.save(dao);
        //       }
//        return "redirect:/admin";

}
