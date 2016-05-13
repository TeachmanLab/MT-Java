package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.domain.PiParticipant;
import edu.virginia.psyc.pi.persistence.PiParticipantRepository;
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
public class PIPlayerController extends BaseController {

    @Autowired private static final Logger LOG = LoggerFactory.getLogger(PIPlayerController.class);

    @Autowired private PiParticipantRepository piParticipantRepository;


    @Autowired
    public PIPlayerController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @RequestMapping(value="{scriptName}", method=RequestMethod.GET)
    public String showPlayer(ModelMap model, Principal principal, @PathVariable String scriptName) {

        PiParticipant p = piParticipantRepository.findByEmail(principal.getName());

        // The Neutral condition requires a completely different file.
        LOG.debug("The Script name: " + scriptName + "!=" +  "RecognitionRatings?" + (scriptName != "RecognitionRatings"));

        if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.NEUTRAL) &&
                !scriptName.equals("RecognitionRatings")) {
            scriptName = scriptName + "NT";
        }

        model.addAttribute("script", scriptName);
        model.addAttribute("sessionName", p.getStudy().getCurrentSession().getName());
        model.addAttribute("participantId", p.getId());
        model.addAttribute("cbmCondition", p.getCbmCondition().toString());
        return "PIPlayer";
    }

    @RequestMapping("/completed/{scriptName}")
    public RedirectView markComplete(Principal principal, @PathVariable String scriptName) {

        Participant participant = getParticipant(principal);

        participant.getStudy().completeCurrentTask();
        participantRepository.save(participant);

        return new RedirectView("/session/next");
    }

}


