package edu.virginia.psyc.templeton.controller;


import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import edu.virginia.psyc.templeton.service.TempletonEmailService;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.ParticipantService;
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
@RequestMapping("questions/ExpectancyBias")
public class ExpectancyController extends QuestionController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(ExpectancyController.class);

    @Autowired
    private ExpectancyBiasRepository ebRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private TempletonEmailService emailService;

    /**
     * Nothing much here, this just redirects back to the main QuestionControllers GET Form logic.
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showExpectancy(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        TempletonStudy study = (TempletonStudy) p.getStudy();
        return showForm(model,principal,"ExpectancyBias");
    }

    @RequestMapping(method = RequestMethod.POST)
    RedirectView handleEB(@ModelAttribute("ExpetancyBias") ExpectancyBias eb,
                          Principal principal,
                          ModelMap model,
                          WebRequest request) throws Exception {

        // Save the form, associate it with the participant, and log the
        // process.
        saveForm("ExpectancyBias", request, model, principal);

        Participant p = participantService.get(principal);

        // If the users score differs from there original score and places the user
        // "at-risk", then send a message to the administrator.
        List<ExpectancyBias> previous = ebRepository.findByParticipant(p);
        if(previous.size() > 0) {
            ExpectancyBias firstEntry = Collections.min(previous);
            Participant participant = participantService.get(principal);
            TempletonStudy study = (TempletonStudy)participant.getStudy();
            if(eb.atRisk(firstEntry)) {
                if (!study.isAtRisk()) { // alert admin the first time.
                    emailService.sendAtRiskAdminEmail(participant, firstEntry, eb);
                    study.setAtRisk(true);
                    participantService.save(participant);
                }
                return new RedirectView("/session/atRisk", true);
            }
        }
        return new RedirectView("/session/next", true);
    }




}
