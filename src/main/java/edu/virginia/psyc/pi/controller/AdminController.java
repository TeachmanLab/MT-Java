package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
public class AdminController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private static final int PER_PAGE=2;

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

        Page<ParticipantDAO> participants;
        PageRequest pageRequest;
        int page = Integer.parseInt(pageParam);

        pageRequest = new PageRequest(page, PER_PAGE);

        if(search.isEmpty()) {
            participants = participantRepository.findAll(pageRequest);
        } else {
            participants = participantRepository.search(search, pageRequest);
        }

        model.addAttribute("participants", participants);
        model.addAttribute("search", search);
        return "admin";

    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String showForm(ModelMap model,
                           @PathVariable("id") long id) {
        ParticipantDAO p;
        p = participantRepository.findOne(id);
        model.addAttribute("participant", p);
        return "participant_form";
    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.POST)
    public String checkParticipantInfo(ModelMap model,
                                       @PathVariable("id") long id,
                                       @Valid Participant participant,
                                       BindingResult bindingResult) {
        ParticipantDAO dao;

        dao = participantRepository.findOne(id);

        if (bindingResult.hasErrors()) {
            LOG.error("Invalid participant:" + bindingResult.getAllErrors());
            model.addAttribute("participant", participant);
            return "participant_form";
        } else {
            dao.setAdmin(participant.isAdmin());
            dao.setEmail(participant.getEmail());
            dao.setFullName(participant.getFullName());
            participantRepository.save(dao);
        }
        return "redirect:/admin";
    }

    @RequestMapping(value="/sendEmail/{type}")
    public String sendEmail(ModelMap model, Principal principal,
                            @PathVariable("type") EmailService.TYPE type) throws Exception {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()).get(0));

        this.emailService.sendSimpleMail(p, type);
        return "redirect:/admin";
    }

}
