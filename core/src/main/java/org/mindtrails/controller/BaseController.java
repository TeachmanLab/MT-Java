package org.mindtrails.controller;

import org.mindtrails.domain.Participant;
import org.mindtrails.service.ImportService;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

/**
 * Adds model attributes to all views for common parameters.
 */
public class BaseController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ImportService importService;

    @ModelAttribute("participant")
    public Participant getParticipant(Principal principal) {
        return participantService.get(principal);
    }

    @ModelAttribute("visiting")
    public boolean visiting(Principal principal) {
        return participantService.get(principal) == null;
    }

    @ModelAttribute("importMode")
    public boolean importMode() {
        return importService.isImporting();
    }



}
