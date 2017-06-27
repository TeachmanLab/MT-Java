package edu.virginia.psyc.r01.controller;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.domain.R01StudyForm;
import org.mindtrails.controller.BaseController;
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
public class R01StudyController extends BaseController {

    @Autowired
    ParticipantService participantService;

    @Autowired
    ParticipantRepository participantRepository;

    private static final String FORM_NAME="studyForm";

    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.GET)
    public String studyUpdateForm(ModelMap model,
                              @PathVariable("id") long id) {

        Participant p = participantRepository.findOne(id);
        R01StudyForm form = new R01StudyForm((R01Study) p.getStudy());
        model.addAttribute("participant", p);
        model.addAttribute(FORM_NAME, form);
        return "admin/studyUpdate";
    }

    @RequestMapping(value="/admin/study/{id}", method= RequestMethod.POST)
    public String updateStudy(ModelMap model,
                              @PathVariable("id") long id,
                              @ModelAttribute(FORM_NAME) R01StudyForm form) {

        Participant p = participantRepository.findOne(id);
        form.updateStudy((R01Study) p.getStudy());
        participantService.save(p);
        return "redirect:/admin";
    }
}
