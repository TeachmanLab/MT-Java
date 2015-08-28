package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
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

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        ParticipantDAO dao = (ParticipantDAO) event.getAuthentication().getPrincipal();
        dao = participantRepository.findByEmail(dao.getEmail()); // Refresh session object from database.
        dao.setLastLoginDate(new Date());
        participantRepository.save(dao);
    }

}