package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.domain.PiParticipant;
import edu.virginia.psyc.r34.domain.forms.PiParticipantListForm;
import edu.virginia.psyc.r34.persistence.PiParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by dan on 7/12/16.
 */
@Controller
public class ConditionController {

    @Autowired
    PiParticipantRepository piParticipantRepository;

    @RequestMapping(value="/updateParticipants", method= RequestMethod.POST)
    public String updateParticipants(ModelMap model,
                                     @ModelAttribute("participants") PiParticipantListForm participantForm) {

        List<PiParticipant> participants = participantForm.getParticipants();
        List<String> sessions = participantForm.getSessionNames();
        int     index;
        PiParticipant participant;

        // We only want to update a very limited set of fields on the participant
        // data model.
        if(null != participants && participants.size() > 0) {
            for (PiParticipant p : participants) {
                index = participants.indexOf(p);
                participant = piParticipantRepository.findOne(p.getId());
                participant.setActive(p.isActive());
                participant.setPrime(p.getPrime());
                participant.setCbmCondition(p.getCbmCondition());
                // Only if the session was change in the ui, update the session
                // current session for the participant, and reset their progress.
                // set the last session date to null so they don't get a timeout
                // message.
                if(p.getStudy().getCurrentSession().getName() != sessions.get(index)) {
                    p.getStudy().forceToSession(sessions.get(index));
                }
                piParticipantRepository.save(participant);
            }
        }
        return "redirect:/admin";
    }
}
