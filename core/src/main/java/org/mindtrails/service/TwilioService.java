package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.domain.TextMessage;

import java.util.List;

/**
 * Sends (and, potentially receives) Text Messages through the Twilio Service.
 * You will need a Twilio account and phone number.
 *
 */
public interface TwilioService {

    /**
     * Sends a text message to the given participant;
     *
     * @param textMessage
     * @param participant
     */
    public void sendMessage(TextMessage textMessage, Participant participant);


    /**
     * Returns a list of possible messages that we can send out.
     *
     * @return
     */
    public List<ScheduledEvent> messageTypes();
}
