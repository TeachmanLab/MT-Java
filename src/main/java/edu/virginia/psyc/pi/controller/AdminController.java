package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.GiftLog;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.ParticipantForm;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.domain.json.TrialJson;
import edu.virginia.psyc.pi.domain.tango.*;
import edu.virginia.psyc.pi.persistence.*;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.TrialDAO;
import edu.virginia.psyc.pi.persistence.TrialRepository;
import edu.virginia.psyc.pi.service.EmailService;
import edu.virginia.psyc.pi.domain.tango.Account;
import edu.virginia.psyc.pi.domain.tango.AccountResponse;
import edu.virginia.psyc.pi.service.TangoService;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private TrialRepository trialRepository;

    @Autowired
    private TangoService tangoService;


    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public AdminController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listUsers(ModelMap model,
                            final @RequestParam(value = "search", required = false, defaultValue = "") String search,
                            final @RequestParam(value = "page", required = false, defaultValue = "0") String pageParam) {

        ParticipantForm form;
        Page<ParticipantDAO> daoList;
        PageRequest pageRequest;

        int page = Integer.parseInt(pageParam);

        pageRequest = new PageRequest(page, PER_PAGE);

        if(search.isEmpty()) {
            daoList = participantRepository.findAll(pageRequest);
        } else {
            daoList = participantRepository.search(search, pageRequest);
        }

        form = new ParticipantForm();
        for(ParticipantDAO dao : daoList) {
            form.add(participantRepository.entityToDomain(dao));
            form.add(dao.getCurrentSession());
        }

        model.addAttribute("participantForm", form);
        model.addAttribute("search", search);
        model.addAttribute("paging", daoList);
        return "admin/admin";

    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String showForm(ModelMap model,
                           @PathVariable("id") long id) {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findOne(id));

        model.addAttribute("participant", p);
        return "admin/participant_form";
    }

    @RequestMapping(value="/updateParticipants", method=RequestMethod.POST)
    public String updateParticipants(ModelMap model,
                                     @ModelAttribute("participants") ParticipantForm participantForm) {

        List<Participant> participants = participantForm.getParticipants();
        List<String> sessions = participantForm.getSessionNames();
        int     index;
        ParticipantDAO dao;

        // We only want to update a very limited set of fields on the participant
        // data model.
        if(null != participants && participants.size() > 0) {
            for (Participant p : participants) {
                index = participants.indexOf(p);
                dao = participantRepository.findOne(p.getId());
                dao.setActive(p.isActive());
                dao.setAdmin(p.isAdmin());
                dao.setPrime(p.getPrime());
                dao.setCbmCondition(p.getCbmCondition());
                // Only if the session was change in the ui, update the session
                // current session for the participant, and reset their progress.
                // set the last session date to null so they don't get a timeout
                // message.
                if(p.getStudy().getCurrentSession().getName() != sessions.get(index)) {
                    dao.setCurrentSession(sessions.get(index));
                    dao.setTaskIndex(0);
                    dao.setLastSessionDate(null);
                }
                participantRepository.save(dao);
            }
        }
        return "redirect:/admin";
    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.POST)
    public String checkParticipantInfo(ModelMap model,
                                       @PathVariable("id") long id,
                                       @Valid Participant participant,
                                       BindingResult bindingResult) {
        ParticipantDAO dao;

        dao = participantRepository.findOne(id);

//        if (bindingResult.hasErrors()) {
//            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
//            model.addAttribute("participant", participant);
//            return "admin/participant_form";
 //       } else {
            participantRepository.domainToEntity(participant, dao);
            participantRepository.save(dao);
 //       }
        return "redirect:/admin";
    }

    @RequestMapping(value="/new_participant", method=RequestMethod.GET)
    public String showNewForm(ModelMap model) {
        Participant p;
        p = new Participant();
        model.addAttribute("participant", p);
        return "admin/new_participant";
    }


    @RequestMapping(value="/participant/", method=RequestMethod.POST)
    public String createParticipant(ModelMap model,
                                       @Valid Participant participant,
                                       BindingResult bindingResult) {

        ParticipantDAO dao;

        if(participantRepository.findByEmail(participant.getEmail()) != null) {
            bindingResult.addError(new ObjectError("email", "This email already exists."));
        }

        if(!participant.getPassword().equals(participant.getPasswordAgain())) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match."));
        }

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            return "admin/new_participant";
        }

        participant.setLastLoginDate(new Date()); // Set the last login date.
        saveParticipant(participant);

        LOG.info("Participant created.");
        return "redirect:/admin";
    }


    @RequestMapping(value="/listEmails", method=RequestMethod.GET)
    public String listEmails(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        return "admin/listEmails";
    }

    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") EmailService.TYPE type) throws Exception {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()));

        if(type.equals(EmailService.TYPE.giftCard)) {
            // Reward reward = tangoService.createGiftCard(p);  This would actually award a gift card, if you need to do some testing.
            Reward reward = new Reward("111-111-111-111", "1234-2345-2345", "123", "https://www.google.com", "12345");
            this.emailService.sendGiftCardEmail(p, reward);
        } else if(type.equals(EmailService.TYPE.dass21Alert)) {
            DASS21_AS d1 = new DASS21_AS(1,1,3,1,3,1,3);
            DASS21_AS d2 = new DASS21_AS(4,4,4,4,4,4,4);
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
        return "admin/listSessions";
    }

    @RequestMapping(value="/listDownloads", method=RequestMethod.GET)
    public String listDownloads(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getStudy().getSessions());
        return "admin/listDownloads";
    }

    /**
     * Returns the json data of a PIPlayer script as a text/csv content
     * @return
     */
    @RequestMapping(value="/playerData", method = RequestMethod.GET, produces = "text/csv")
    @ResponseBody
    public String getData() {
        StringBuffer csv = new StringBuffer();
        List<String> keys;
        Map<String, String> reportData;
        TrialJson trial;
        List<TrialDAO> trialData = trialRepository.findAll();

        // Write headers based on first trial.
        keys = TrialJson.interpretationReportHeaders();
        for (String k : keys) {
            csv.append(k);
            csv.append(",");
        }
        csv.append(("\n"));

        // Write the data.
        for (TrialDAO data : trialData) {
            reportData = data.toTrialJson().toInterpretationReport();
            for (String k : keys) {
                csv.append("\"");
                if(null == reportData.get(k)) {
                    csv.append("");
                } else {
                    csv.append(reportData.get(k).replaceAll("\"", "\\\""));
                }
                csv.append("\"");
                csv.append(",");
            }
            csv.append("\n");
        }
        return csv.toString();
    }


    // Trying to write a methods to get Tango Account information. By Diheng

    @RequestMapping(value="/checkFunds",method = RequestMethod.GET)
    public String checkFunds(ModelMap model, Principal principal){
        Account a = tangoService.getAccountInfo();
        model.addAttribute("tango",a);
        return "admin/checkFunds";
    }

    // Added by Diheng, try to send gift card to participants;

    @RequestMapping(value="/participant/giftCard")
    public String giftCard(ModelMap model, Principal principal) throws Exception {
        Participant p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()));
        Reward r = tangoService.createGiftCard(p, "AdminAwarded");
        this.emailService.sendGiftCardEmail(p, r);
        model.addAttribute("participant",p);
        return "/admin/participant_form";
    }


    // Added by Diheng, try to recall reward information by order ID;
    @RequestMapping(value="/rewardInfo/{orderId}", method = RequestMethod.GET)
    public String showRewardInfo(ModelMap model, Principal principal, @PathVariable ("orderId") String orderId) {
        Order order = tangoService.getOrderInfo(orderId);
        model.addAttribute("order",order);
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
