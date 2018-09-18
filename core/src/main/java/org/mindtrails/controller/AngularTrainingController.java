package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.WrongFormException;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

/**
 * Created by dan on 7/7/16.
 */
@Controller
@RequestMapping("/angularTraining")
public class AngularTrainingController extends BaseController {

    @Autowired
    ParticipantService participantService;

    private static final Logger LOG = LoggerFactory.getLogger(AngularTrainingController.class);

    @RequestMapping(value="{scriptName}", method= RequestMethod.GET)
    public String showTraining(ModelMap model, Principal principal, @PathVariable String scriptName) {

        Participant p = participantService.get(principal);
        model.addAttribute("sessionName", p.getStudy().getCurrentSession().getName());
        return "redirect:/angular_training/index.html/#/" + scriptName.toLowerCase();
    }

    @RequestMapping("/completed")
    public RedirectView markComplete(ModelMap model, Principal principal) {
        Participant participant = participantService.get(principal);

        // If the data submitted, isn't the data the user should be completing right now,
        // throw an exception and prevent them from moving forward.
        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals("Training") && !participant.isAdmin()) {
            String error = "The current task for this participant is : " + currentTaskName + " however, they completed the angular Training";
            LOG.info(error);
            throw new WrongFormException(error);
        }

        // Fixme: Calculate time spent on the training session
        participant.getStudy().completeCurrentTask(0);
        participantService.save(participant);

        return new RedirectView("/session/next", true);

    }


}

