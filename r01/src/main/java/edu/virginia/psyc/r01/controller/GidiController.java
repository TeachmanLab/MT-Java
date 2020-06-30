package edu.virginia.psyc.r01.controller;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.Gidi;
import edu.virginia.psyc.r01.persistence.GidiRepository;
import edu.virginia.psyc.r01.persistence.OA;
import edu.virginia.psyc.r01.persistence.OARepository;
import edu.virginia.psyc.r01.service.R01EmailService;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.StudyRepository;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * Overrides the basic Question Controller to the handle the Gidi Study - as
 * this may change the users condition assignment.
 *
 * */
@Controller
@RequestMapping("questions/gidi")
public class GidiController extends QuestionController {

    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(GidiController.class);

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private GidiRepository gidiRepository;

    @Autowired
    private StudyRepository studyRepository;

    /**
     * Nothing much here, this just redirects back to the main QuestionControllers GET Form logic.
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String showGidi(ModelMap model, Principal principal) {
        return showForm(model, principal,"gidi");
    }

    @RequestMapping(method = RequestMethod.POST)
    RedirectView handleGidi(@ModelAttribute("Gidi") Gidi gidi,
                          Principal principal,
                          ModelMap model,
                          Device device,
                          @RequestHeader(value="User-Agent", defaultValue="foo") String userAgent,
                          WebRequest request) throws Exception {


        handleForm("Gidi", request, model, principal, device, userAgent);
        Participant participant = participantService.get(principal);
        if(gidi.getAccepted() != null && gidi.getAccepted().equals("true")) {
            R01Study study = (R01Study) participant.getStudy();
            study.setStudyExtension(R01Study.STUDY_EXTENSIONS.GIDI.name());
            study.setReceiveGiftCards(true);  // Turns gift cards on for the study,
            // but gift cards will also need to be set to true for participant as
            // well once they are verified, which will happen during the next steps.
            studyRepository.save(study);

            return new RedirectView("/account/changePhone", true);
        }
    return new RedirectView("/session/next", true);
    }
}
