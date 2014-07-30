package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;

import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/29/14
 * Time: 10:48 AM
 * Provides some common behavior useful to all controllers.
 */
public class BaseController {

    protected ParticipantRepository participantRepository;

    Participant getParticipant(Principal principal) {
        Participant p;
        p = participantRepository.entityToDomain(participantRepository.findByEmail(principal.getName()).get(0));
        return(p);
    }

    Participant getParticipant(String email) {
        Participant p;
        List<ParticipantDAO> list;
        list = participantRepository.findByEmail(email);
        if(list.size() > 0) return(participantRepository.entityToDomain(list.get(0)));
        return null;
    }

    void saveParticipant(Participant participant) {
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
