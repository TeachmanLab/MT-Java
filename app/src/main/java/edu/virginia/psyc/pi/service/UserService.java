package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * This User Service is able to catch a successful log in event.  Useful for anything that should
 * happen when a user logs in to the system.
 */
@Service
public class UserService implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        Participant participant = (Participant) event.getAuthentication().getPrincipal();
        LOG.info("Authentication Success Event Captured for " + participant.getEmail());
        participant = participantRepository.findByEmail(participant.getEmail()); // Refresh session object from database.
        participant.setLastLoginDate(new Date());
        participantRepository.save(participant);
    }

}