package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Reward;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * Created by dan on 5/13/16.
 */
public interface EmailService {

    enum TYPE { GIFTCARD, PASSWORD_RESET, ALERT_ADMIN }

    /**
     * This should send an email to an administrative account
     * notifying them that data is not being exported from the
     * system and is starting to pile up.
     * @param message
     */
    void sendExportAlertEmail(String message);

    /**
     * Executed when someone asks to reset their password.
     * The provided participant will be fully populated based
     * on the given password.
     * @param participant
     */
    void sendPasswordReset(Participant participant) throws MessagingException;

    /**
     * Executed when someone should receive a new gift card.  Reward object should
     * contain details on how to obtain the gift card.
     * @param participant
     */
    void sendGiftCard(Participant participant, Reward reward, int amountCents) throws MessagingException;

    /**
     * Send an atRisk email message if the participant is performing
     * low on some metric.
     */
    void sendAtRiskAlertToAdmin(Participant participant, String details);

    /**
     * Send a custom email message, of the specified type to
     * the Participant.
     * @param participant
     * @param type
     */
    void sendEmail(Participant participant, String type);

    /**
     * Returns a list of email messages this system can produce.  Along with
     * descriptions for what each email can do.  Used
     * by the admin interface to provide links so custom email messages
     * can be sent out.
     */
    Map<String,String> emailTypes();


}
