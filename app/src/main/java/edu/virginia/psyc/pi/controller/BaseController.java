package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.PiParticipant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
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

    ParticipantDAO getParticipantDAO(Principal principal) {
        return participantRepository.findByEmail(principal.getName());
    }

    ParticipantDAO getParticipantDAO(String email) {
        return participantRepository.findByEmail(email);
    }

    PiParticipant getParticipant(Principal principal) {
        return participantRepository.entityToDomain(getParticipantDAO(principal));
    }

    PiParticipant getParticipant(ParticipantDAO dao) {
        return participantRepository.entityToDomain(dao);
    }

    PiParticipant getParticipant(String email) {
        PiParticipant p;
        ParticipantDAO dao;
        dao = participantRepository.findByEmail(email);
        if(dao != null) return(participantRepository.entityToDomain(dao));
        return null;
    }

    void saveParticipant(PiParticipant participant) {
        ParticipantDAO dao;

        if(participant.getId() > 0) {
            dao = participantRepository.findOne(participant.getId());
        } else {
            dao = new ParticipantDAO();
        }

        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);
    }

}
