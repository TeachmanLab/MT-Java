package edu.virginia.psyc.mindtrails.service;

import edu.virginia.psyc.mindtrails.domain.Participant;

import javax.mail.MessagingException;

/**
 * Created by dan on 5/13/16.
 */
public interface EmailService {

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

}
