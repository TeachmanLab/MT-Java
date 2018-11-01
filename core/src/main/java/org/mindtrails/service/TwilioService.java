package org.mindtrails.service;

import org.mindtrails.domain.Participant;

/**
 * Sends (and, potentially receives) Text Messages through the Twilio Service.
 * You will need a Twilio account and phone number.
 *
 */
public interface TwilioService {

    /**
     * Sends a text message to the given participant;
     * @param textMessage
     * @param participant
     */
    public void sendMessage(String textMessage, Participant participant);


    /**
     * Given a participant, determines which message to send that
     * participant.  If no message should be sent, returns Null.
     * @param p
     * @return
     */
    public String getMessage(Participant p);

}