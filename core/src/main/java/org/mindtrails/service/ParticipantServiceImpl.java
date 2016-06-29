package org.mindtrails.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Basic implementation of the Participant Service, offers some basic
 * settings.
 */
@Service
public abstract class ParticipantServiceImpl implements ParticipantService {

    @Value("${tango.maxParticipants}")
    private long maxParticipantsForGiftCards;

    public boolean receiveGiftCards() {
        return maxParticipantsForGiftCards > this.count();
    }

}
