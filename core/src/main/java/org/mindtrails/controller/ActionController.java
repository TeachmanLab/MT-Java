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

/* 

    In the Action repository, create 1 entry per 1 action object that includes:
        - The participant id
        - The questionnaire name
        - The action name
        - The action timestamp

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
    @RequestMapping(value="/questionnaire/{formName}", method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody
    ResponseEntity<Void> saveActionSequence(Principal principal,
                 @PathVariable("formName") String formName, @RequestBody ActionList actionList) {

        String message = "Saving action sequence to database for questionnaire..." + formName;
        LOG.info(message);

        Participant p = getParticipant(principal);
        Session session = p.getStudy().getCurrentSession();

        for(ActionList action: list) {
            action.setParticipant(p);
            action.setDate(new Date());
            action.setSession(session));
            this.actionRepository.save(action);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /* Should we save participant average latency as well? This would come in handy as a feature. */

    /* For later implementation, if we want to view action sequences in the web dashboard */

    /* 
    @RequestMapping(value="/questionnaire/{id}", method = RequestMethod.GET)
    public String getQuestionnaireLatencies(ModelMap model, @PathVariable("id") long id) 
    
    } 
    */
}


