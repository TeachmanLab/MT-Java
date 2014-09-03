package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.ParticipantForm;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.service.EmailService;
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

    private static final int PER_PAGE=10; // Number of users to display per page.

    @Autowired
    private EmailService emailService;


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
        List<Session.NAME> sessions = participantForm.getSessionNames();
        Session session;
        int     index;
        ParticipantDAO dao;

        if(null != participants && participants.size() > 0) {
            for (Participant p : participants) {
                index = participants.indexOf(p);
                // Only if the session was change in the ui, update the session
                // current session for the participant, and reset their progress.
                if(p.getCurrentSession().getName() != sessions.get(index)) {
                    p.setSessions(Session.createListView(sessions.get(index), 0));
                }
                dao = participantRepository.findOne(p.getId());
                participantRepository.domainToEntity(p, dao);
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

        this.emailService.sendSimpleMail(p, type);
        return "redirect:/admin";
    }

    @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = getParticipant(principal);
        model.addAttribute("participant", p);
        model.addAttribute("sessions", p.getSessions());
        return "admin/listSessions";
    }


}
