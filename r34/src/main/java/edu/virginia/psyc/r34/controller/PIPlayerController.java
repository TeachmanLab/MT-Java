package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.domain.R34Study;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.WrongFormException;
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

    @Autowired private static final Logger LOG = LoggerFactory.getLogger(PIPlayerController.class);

    @Autowired
    private ParticipantService participantService;


    @RequestMapping(value="{scriptName}", method=RequestMethod.GET)
    public String showPlayer(ModelMap model, Principal principal, @PathVariable String scriptName) {

        Participant p = participantService.findByEmail(principal.getName());
        R34Study study   = (R34Study)p.getStudy();

        // The Neutral condition requires a completely different file.
        LOG.debug("The Script name: " + scriptName + "!=" +  "RecognitionRatings?" + (scriptName != "RecognitionRatings"));

        if(study.getCondition().equals(R34Study.CONDITION.NEUTRAL) &&
                !scriptName.equals("RecognitionRatings")) {
            scriptName = scriptName + "NT";
        }

        model.addAttribute("script", scriptName);
        model.addAttribute("sessionName", p.getStudy().getCurrentSession().getName());
        model.addAttribute("participantId", p.getId());
        model.addAttribute("condition", study.getCondition().toString());
        return "PIPlayer";
    }

    @RequestMapping("/completed/{scriptName}")
    public RedirectView markComplete(Principal principal, @PathVariable String scriptName) {

        Participant participant = participantService.get(principal);

        // If the data submitted, isn't the data the user should be completeing right now,
        // thown an exception and prevent them from moving forward.
        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals(scriptName)) {
            LOG.info("The current task for this participant is : " + currentTaskName + " however, they submitted the script:" + scriptName);
            throw new WrongFormException();
        }

        participant.getStudy().completeCurrentTask();
        participantService.save(participant);
        return new RedirectView("/session/next");
    }

}


