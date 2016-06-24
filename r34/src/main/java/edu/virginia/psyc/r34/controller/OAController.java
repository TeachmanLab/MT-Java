package edu.virginia.psyc.r34.controller;

import org.mindtrails.controller.QuestionController;
import edu.virginia.psyc.r34.domain.PiParticipant;
import edu.virginia.psyc.r34.persistence.PiParticipantRepository;
import edu.virginia.psyc.r34.persistence.Questionnaire.OA;
import edu.virginia.psyc.r34.persistence.Questionnaire.OARepository;
import edu.virginia.psyc.r34.persistence.Questionnaire.ReasonsForEnding;
import edu.virginia.psyc.r34.service.PiEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Overrides the basic Question Controller to the handle the OA Submittion - as
 * this form requies us to track the users status through the study and alert
 * the participant as well as send an email to the administrator.
 *
 * */
@Controller
@RequestMapping("questions/OA")
public class OAController extends QuestionController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(OAController.class);

    @Autowired
    private OARepository oaRepository;

    @Autowired
    private PiParticipantRepository piParticipantRepository;

    @Autowired
    private PiEmailService emailService;

    /**
     * Nothing much here, this just redirects back to the main QuestionControllers GET Form logic.
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showOA(ModelMap model, Principal principal) {
        PiParticipant p = piParticipantRepository.findByEmail(principal.getName());
        model.addAttribute("inSessions", p.inSession());
        return showForm(model,principal,"OA");
    }

    @RequestMapping(method = RequestMethod.POST)
    RedirectView handleOA(@ModelAttribute("OA") OA oa,
                          Principal principal,
                          WebRequest request) throws Exception {

        // Save the form, associate it with the participant, and log the
        // process.
        saveForm("OA", request);

        // If the users score differs from there original score and places the user
        // "at-risk", then send a message to the administrator.
        List<OA> previous = oaRepository.findByParticipant(oa.getParticipant());
        OA firstEntry = Collections.min(previous);

        PiParticipant participant = piParticipantRepository.findByEmail(principal.getName());
        if(oa.atRisk(firstEntry)) {
            if(!participant.isIncrease50()) { // alert admin the first time.
                emailService.sendAtRiskAdminEmail(participant, firstEntry, oa);
                participant.setIncrease50(true);
                piParticipantRepository.save(participant);
            }
            return new RedirectView("/session/atRisk");
        }
        return new RedirectView("/session/next");
    }


    /**
     * Bypass the session check, this form can be completed at
     * any time.
     */
    @RequestMapping(value = "ReasonsForEnding", method = RequestMethod.POST)
    String handleReasonsForEnding(@ModelAttribute("ReasonsForEnding") ReasonsForEnding reasons,
                                  WebRequest request) throws Exception {

        recordSessionProgress("ReasonsForEnding",reasons, true);
        saveForm("reasons", request, false);
        return "debriefing";
    }



}
