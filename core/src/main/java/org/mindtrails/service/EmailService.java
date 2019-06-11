package org.mindtrails.service;

import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.GiftLog;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Created by dan on 5/13/16.
 */
public interface EmailService {

    /**
     * This should send an email to an administrative account
     * notifying them of some problem with the system.
     * @param message
     */
    void sendAdminEmail(String subject, String message);

    /**
     * Executed when someone asks to reset their password.
     * The provided participant will be fully populated based
     * on the given password.
     * @param participant
     */
    void sendPasswordReset(Participant participant);

    /**
     * Executed when someone should receive a new gift card.  Reward object should
     * contain details on how to obtain the gift card.
     * @param participant
     */
    void sendGiftCard(Participant participant, OrderResponse reward, GiftLog log);

    /**
     * Send a custom email message, of the specified type to
     * the Participant.
     * @param email The email object should have a type, subject, address, and context.  If a
     *              participant is specified, it will be passed on to the view.
     */
    void sendEmail(Email email) throws MessagingException;

    /**
     * Returns a list of email messages this system can produce.  Along with
     * descriptions for what each email can do.  Used
     * by the admin interface to provide links so custom email messages
     * can be sent out.
     */
    List<Email> emailTypes();

    /**
     * Sends an exame email message to an administrator, so they can see what the
     * recipient will see.
     */
    void sendExample(Email email) throws MessagingException;

    /**
     * Returns an email for a given type
     */
    Email getEmailForType(String type);

    void sendSessionCompletedEmail(Participant participant);


}
