package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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
public class LoginController {

    private ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    /**
     * Spring automatically configures this object.
     * You can modify the location of this database by editing the application.properties file.
     */
    @Autowired
    public LoginController(ParticipantRepository repository) {
        this.participantRepository   = repository;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String printWelcome(ModelMap model, Principal principal ) {
        return "redirect:/session";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        model.addAttribute("participant", new Participant());
        return "login";

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

            return "login";
        } else {
            StandardPasswordEncoder encoder = new StandardPasswordEncoder();
            String hashedPassword = encoder.encode(participant.getPassword());

            ParticipantDAO dao = new ParticipantDAO();
            participantRepository.domainToEntity(participant, dao);
            dao.setPassword(hashedPassword);
            participantRepository.save(dao);
            participantRepository.flush();
        }

        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), participant.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOG.info("Participant authenticated.");
        return "redirect:/session";
    }


    @RequestMapping(value="/loginfailed", method = RequestMethod.GET)
    public String loginerror(ModelMap model) {

        model.addAttribute("error", "true");
        return "login";

    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logout(ModelMap model) {
        return "login";
    }


}