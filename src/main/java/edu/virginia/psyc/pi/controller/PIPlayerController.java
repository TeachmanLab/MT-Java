package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:04 PM
 * Executes a PIPlayer Script.  It uses the resources/templates/PIPlayer.html script
 * to include a Javascript file located in resources/static/PIPlayerScripts/[XXX]
 * Where XXX is the script name passed in to the showPlayer method.
 * You should modify the PIPlayer script to have a return value that will bring you
 * back to this controller at /playerScript/completed/[XXX]  This will mark this
 * portion of the session complete, and take you back the session page.
 *
 *     API.addSettings('redirect', "../playerScript/completed/int_train");
 *
 */
@Controller
@RequestMapping("/playerScript")
public class PIPlayerController {

    private ParticipantRepository participantRepository;

    @Autowired
    public PIPlayerController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @RequestMapping(value="{scriptName}", method=RequestMethod.GET)
    public String showPlayer(ModelMap model, Principal principal, @PathVariable String scriptName) {
        model.addAttribute("script", scriptName);
        return "PIPlayer";
    }

    @RequestMapping("/completed/{scriptName}")
    public RedirectView markComplete(Principal principal, @PathVariable String scriptName) {

        ParticipantDAO dao      = (ParticipantDAO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = participantRepository.entityToDomain(dao);

        participant.completeCurrentTask();
        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);

        return new RedirectView("/session");
    }

}


