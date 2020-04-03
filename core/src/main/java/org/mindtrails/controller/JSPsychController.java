package org.mindtrails.controller;

import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.ImportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.RestExceptions.WrongFormException;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.jsPsych.JsPsychTrial;
import org.mindtrails.domain.jsPsych.JsPsychTrialList;
import org.mindtrails.persistence.JsPsychRepository;
import org.mindtrails.service.ImportService;
import org.mindtrails.service.ParticipantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;
import java.util.List;

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


    @RequestMapping("/status")
    public @ResponseBody JsPsychTrialList getPastData(Principal principal) {

        Participant participant = participantService.findByEmail(principal.getName());

        List<JsPsychTrial> trials = jsPsychRepository.findAllByParticipantAndSession(participant,
                participant.getStudy().getCurrentSession().getName());

        JsPsychTrialList list = new JsPsychTrialList();
        list.addAll(trials);

        return (list);
    }

    @ExportMode
    @RequestMapping("/completed/{scriptName}")
    public RedirectView markComplete(Principal principal, @PathVariable String scriptName,
                                     Device device,
                                     @RequestHeader(value="User-Agent", defaultValue="foo") String userAgent) {

        Participant participant = participantService.get(principal);

        // If the data submitted, isn't the data the user should be completing right now,
        // throw an exception and prevent them from moving forward.
        String currentTaskName = participant.getStudy().getCurrentSession().getCurrentTask().getName();
        if(!currentTaskName.equals(scriptName) && !participant.isAdmin()) {
            String error = "The current task for this participant is : " + currentTaskName + " however, they submitted the script:" + scriptName;
            LOG.info(error);
            throw new WrongFormException(error);
        }

        // Calculate the time on task
        List<JsPsychTrial> trials = jsPsychRepository.findAllByParticipantAndSession(participant,
                participant.getStudy().getCurrentSession().getName());

        // If no data exists in the trails, then something went wrong - a session timeout or other issue occurred.  They
        // will need to complete the trials to move forward.
        if(trials.size() < 10) {
            String error = "Recognition rations were not completed in full.";
            LOG.info(error);
            throw new WrongFormException(error);
        }


        // Find the greatest time_elapsed value for the data returned, this is time spent on the trial.
        double timeOnTask = 0.0;
        for (JsPsychTrial trial : trials) {
            if(trial.getTime_elapsed() > timeOnTask)
            timeOnTask = trial.getTime_elapsed();
        }



        participant.getStudy().completeCurrentTask(timeOnTask/1000, device, userAgent);
        participantService.save(participant);

        return new RedirectView("/session/next", true);
    }


    @ExportMode
    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody ResponseEntity<JsPsychTrialList>
        createData(Principal principal,
                   Device device,
                   @RequestBody JsPsychTrialList list) {

        Participant p = getParticipant(principal);
        String deviceType = "unknown";
        if(device != null) {
            if (device.isMobile()) deviceType = "mobile";
            if (device.isNormal()) deviceType = "normal";
            if (device.isTablet()) deviceType = "tablet";
        }
        for(JsPsychTrial trial : list) {
            trial.setParticipant(p);
            trial.setSession(p.getStudy().getCurrentSession().getName());
            trial.setDevice(deviceType);
            trial.setDateSubmitted(new Date());
            trial.setDate(new Date());
            this.jsPsychRepository.save(trial);
        }

        return new ResponseEntity<>(list, HttpStatus.CREATED);
    }



}