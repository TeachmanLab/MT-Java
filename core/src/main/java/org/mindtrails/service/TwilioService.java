package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;

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


    /**
     * Redact the phone number from a string - useful for logging.
     */
    public String redactPhone(String message);
}
