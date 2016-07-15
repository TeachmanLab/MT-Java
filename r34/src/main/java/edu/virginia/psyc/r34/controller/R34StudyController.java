package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.domain.forms.R34StudyForm;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dan on 7/12/16.
 */
@Controller
public class R34StudyController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value="/admin/updateStudy", method= RequestMethod.POST)
    public String updateStudy(ModelMap model,
                              @ModelAttribute("R34StudyForm") R34StudyForm studyForm) {

        // Need to do some thoughtful stuff here maybe.
        return "redirect:/admin";
    }
}
