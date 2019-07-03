package org.mindtrails.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ReasonsForEnding;
import org.mindtrails.persistence.ReasonsForEndingRepository;
import org.mindtrails.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * Created by Anna on 7/2/19
 */

/* We need to do two main things:

    1. Record a participant's average latency between actions. This will be useful for finding predictors of attrition.
    2. In the Action database table, create 1 entry per 1 action duple (action1, action2) that includes:
        - The participant id
        - The questionnaire name
        - The latency time

 */  

@Controller
@RequestMapping("/action")
public class ActionController extends BaseController {

    @Autowired
    ActionRepository actionRepository;

    private static final Logger LOG = LoggerFactory.getLogger(actionRepository.class);

    /* 
        Save the action sequence to the repo, for the given questionnaire
     */
    @ExportMode
    @RequestMapping(value="/questionnaire/{id}", method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody
    ResponseEntity<Void> saveActionSequence(Principal principal,
                 @RequestBody ActionList actionList) {

        LOG.info("Saving action sequence to database for questionnaire...")

        Participant p = getParticipant(principal);

        for(ActionList action: list) {
            action.setParticipant(p);
            action.setDate(new Date());
            this.actionRepository.save(action);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/questionnaire/{id}", method = RequestMethod.GET)
    public String getQuestionnaireLatencies(ModelMap model, @PathVariable("id") long id) {
        




    }

    @RequestMapping(value="/participant/{id}", method=RequestMethod.GET)
    public String participantUpdateForm(ModelMap model,
                           @PathVariable("id") long id) {
        Participant p;
        ParticipantUpdateAdmin form;
        p    = participantRepository.findOne(id);
        form = new ParticipantUpdateAdmin(p);
        model.addAttribute("participantUpdateAdmin", form);
        model.addAttribute("participantEdit", p);
        model.addAttribute("coaches", participantRepository.findCoaches());
        return "admin/participantUpdate";
    }

        @RequestMapping(value="/listSessions", method=RequestMethod.GET)
    public String listSessions(ModelMap model, Principal principal) {
        Participant p = participantService.get(principal);
        model.addAttribute("sessions", p.getStudy().getSessions());
        return "admin/listSessions";
    }



}


        int num_actions = actionList.length
        int latency;
        Date timestamp1, timestamp2;

        for(int i=0; i< num_actions-1; i++) {

            training.setParticipant(p);
            training.setDate(new Date());

            timestamp1 = actionList[i].getTimestamp()
            timestamp2 = actionList[i+1].getTimestamp()
            latency = timestamp1.getTime() - timestamp2.getTime()
        }
        
        averageLatency = averageLatency / float(num_actions)
        averageLatency = (participant.getAverageLatency() + averageLatency) / 2.0




        participant.setAverageLatency(averageLatency);
        participantService.save(participant);

