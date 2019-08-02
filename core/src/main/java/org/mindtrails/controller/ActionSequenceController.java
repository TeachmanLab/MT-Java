package org.mindtrails.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mindtrails.domain.ActionSequence;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ActionSequenceRepository;
import org.mindtrails.persistence.ReasonsForEnding;
import org.mindtrails.persistence.ReasonsForEndingRepository;
import org.mindtrails.service.*;
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
 * Created by Anna on 7/2/19
 */

@Controller
@RequestMapping("/action")
public class ActionSequenceController extends BaseController {

    @Autowired
    ActionSequenceRepository actionSequenceRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ActionSequenceController.class);

    @ExportMode
    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody ResponseEntity<ActionSequence> createActionSequence(Principal principal, 
        @RequestBody ActionSequence actionSequence) {

        Participant p = getParticipant(principal);
        String studyName = p.getStudy().getName();
        String sessionName = p.getStudy().getCurrentSession().getName();
        String taskName =  p.getStudy().getCurrentSession().getCurrentTask().getName();

        String message = "Saving action sequence to database for questionnaire " + taskName;
        LOG.info(message);
        
        actionSequence.setStudyName(studyName);
        actionSequence.setSessionName(studyName);
        actionSequence.setTaskName(studyName);

        this.actionSequenceRepository.save(actionSequence);
    
        return new ResponseEntity<>(actionSequence, HttpStatus.CREATED);
    }
}


