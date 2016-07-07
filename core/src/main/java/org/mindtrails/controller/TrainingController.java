package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * Created by dan on 7/7/16.
 */
@Controller
@RequestMapping("/training")
public class TrainingController {

    @Autowired
    ParticipantService participantService;

    private static final Logger LOG = LoggerFactory.getLogger(TrainingController.class);

    @RequestMapping(value="{scriptName}", method= RequestMethod.GET)
    public String showTraining(ModelMap model, Principal principal, @PathVariable String scriptName) {

        Participant p = participantService.findByEmail(principal.getName());

        model.addAttribute("script", scriptName);
        model.addAttribute("sessionName", p.getStudy().getCurrentSession().getName());
        model.addAttribute("participantId", p.getId());
        return "training/template";
    }

}
