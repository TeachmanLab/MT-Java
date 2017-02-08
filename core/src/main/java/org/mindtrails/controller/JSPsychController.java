package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.WrongFormException;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.domain.jsPsych.JsPsychTrialList;
import org.mindtrails.persistence.JsPsychRepository;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

/**

 * Runs a given CSV file through the JSPsych Script.
 * It uses the resources/templates/jspsych.html script
 * to include a CSV file located in resources/static/jspsych/[XXX]
 *
 */
@Controller
@RequestMapping("/jspsych")
public class JSPsychController extends BaseController {

    @Autowired private static final Logger LOG = LoggerFactory.getLogger(JSPsychController.class);

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private JsPsychRepository jsPsychRepository;

    @RequestMapping(value="{scriptName}", method=RequestMethod.GET)
    public String showPlayer(ModelMap model, Principal principal, @PathVariable String scriptName) {

        Participant p = participantService.findByEmail(principal.getName());
        Study study   = p.getStudy();

        model.addAttribute("script", scriptName);

        for(String key : study.getPiPlayerParameters().keySet()) {
            model.addAttribute(key, study.getPiPlayerParameters().get(key));
        }
        return "jspsych";
    }

    @RequestMapping("/completed/{scriptName}")
    public RedirectView markComplete(Principal principal, @PathVariable String scriptName) {

        Participant participant = participantService.get(principal);

        // If the data submitted, isn't the data the user should be completeing right now,
        // thown an exception and prevent them from moving forward.
        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals(scriptName) && !participant.isAdmin()) {
            LOG.info("The current task for this participant is : " + currentTaskName + " however, they submitted the script:" + scriptName);
            throw new WrongFormException();
        }

        participant.getStudy().completeCurrentTask();
        participantService.save(participant);
        return new RedirectView("/session/next", true);
    }


    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody ResponseEntity<JsPsychTrialList> createData(@RequestBody JsPsychTrialList list) {
        LOG.info("Received " + list.size() + " trials.");
        for(JsPsychTrial trial : list) {
            this.jsPsychRepository.save(trial);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}