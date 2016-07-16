package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.domain.forms.R34StudyForm;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dan on 7/12/16.
 */
@Controller
public class R34StudyController {

    @Autowired
    ParticipantService participantService;

    @Autowired
    ParticipantRepository participantRepository;


    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.GET)
    public String studyUpdateForm(ModelMap model,
                              @PathVariable("id") long id) {

        Participant p = participantRepository.findOne(id);
        R34StudyForm form = new R34StudyForm((R34Study)p.getStudy());
        model.addAttribute("r34StudyForm", form);
        model.addAttribute("participant", p);
        return "admin/studyUpdate";
    }

    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.POST)
    public String updateStudy(ModelMap model,
                              @PathVariable("id") long id,
                              @ModelAttribute("R34StudyForm") R34StudyForm form) {

        Participant p = participantRepository.findOne(id);
        form.updateStudy((R34Study)p.getStudy());
        participantService.save(p);
        return "redirect:/admin";
    }
}
