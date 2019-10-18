package org.mindtrails.controller;

import org.mindtrails.domain.tracking.ActionLog.Action;
import org.mindtrails.domain.tracking.ActionLog.ActionError;
import org.mindtrails.domain.tracking.ActionLog.ActionLog;
import org.mindtrails.domain.tracking.ErrorLog;
import org.mindtrails.persistence.ErrorLogRepository;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ActionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by Anna on 7/2/19
 */

@Controller
@RequestMapping("/action")
public class ActionController extends BaseController {

    @Autowired
    ActionLogRepository actionLogRepository;
    
    @Autowired
    ErrorLogRepository errorLogRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

    @ExportMode
    @RequestMapping(method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody ResponseEntity<ActionLog> createLog(Principal principal,
                                                             @RequestBody Action action) {
        Participant p = getParticipant(principal);
        String studyName = p.getStudy().getName();
        String sessionName = p.getStudy().getCurrentSession().getName();
        String taskName =  p.getStudy().getCurrentSession().getCurrentTask().getName();

        String message = "Saving action sequence to database for questionnaire " + taskName;
        LOG.info(message);

        ActionLog actionLog = new ActionLog(action, studyName, sessionName, taskName, p);
        this.actionLogRepository.save(actionLog);
    
        return new ResponseEntity<>(actionLog, HttpStatus.CREATED);
    }


    @ExportMode
    @RequestMapping(value="/error", method = RequestMethod.POST,
            headers = "content-type=application/json")
    public @ResponseBody ResponseEntity<ErrorLog> recordAjaxException(Principal principal,
                                                             @RequestBody ActionError actionError) {
        Participant p = getParticipant(principal);
        LOG.error(actionError.getMessage());
        ErrorLog errorLog = new ErrorLog(p, actionError.getUrl(), actionError.getException());
        errorLogRepository.save(errorLog);
        return new ResponseEntity<>(errorLog, HttpStatus.CREATED);
    }

}


