package edu.virginia.psyc.pi.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

        return "login";

    }

    @RequestMapping(value = "/newParticipant", method = RequestMethod.POST)
    public String createNewParticipant(final @ModelAttribute edu.virginia.psyc.pi.persistence.ParticipantDAO participant,
                                      final BindingResult result,
                                      final SessionStatus status,
                                      final @RequestParam(value = "unencodedPassword", required = true) String password,
                                      final @RequestParam(value = "unencodedPassword2", required = true) String password2,
                                      ModelMap model) {

        LOG.info("Create New Participant Called.");

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        LOG.info("Password is " + hashedPassword);


        participant.setPassword(hashedPassword);
        participantRepository.save(participant);
        participantRepository.flush();

        LOG.info("Participant saved.");


        // Log this new person in.
        Authentication auth = new UsernamePasswordAuthenticationToken( participant.getEmail(), password);
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