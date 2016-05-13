package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/29/14
 * Time: 10:48 AM
 * Provides some common behavior useful to all controllers.
 */
public class BaseController {

    protected ParticipantRepository participantRepository;
    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    Participant getParticipant(Principal principal) {
        return participantRepository.findByEmail(principal.getName());
    }

    Participant getParticipant(String email) {
        return participantRepository.findByEmail(email);
    }

    void saveParticipant(Participant participant) {
        participantRepository.save(participant);
    }

}
