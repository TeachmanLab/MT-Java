package org.mindtrails.service;


import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * Basic implementation of the Participant Service, offers some basic
 * settings.
 */
@Service
public abstract class ParticipantServiceImpl implements ParticipantService {

    private static final int INACTIVE_AFTER_DAYS=19;

    @Autowired
    private ParticipantRepository participantRepository;

    @Value("${tango.maxParticipants}")
    private long maxParticipantsForGiftCards;

    public boolean receiveGiftCards() {
        return maxParticipantsForGiftCards > this.count();
    }

    @Override
    public void save(Participant p) {
        participantRepository.save(p);
    }

    @Override
    public Participant findByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    @Override
    public List<Participant> findByPhone(String phone) {
        return participantRepository.findByPhone(phone);
    }

    @Override
    public void flush() {
        participantRepository.flush();
    }

    @Override
    public long count() {
        return participantRepository.count();
    }

    @Override
    public Participant get(Principal p) {
        if (p == null) return null;
        return participantRepository.findByEmail(p.getName());
    }
}

