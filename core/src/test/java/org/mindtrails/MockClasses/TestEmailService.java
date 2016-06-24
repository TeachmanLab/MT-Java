package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * Created by dan on 5/13/16.
 */
@Service
public class TestEmailService implements EmailService {

    @Override
    public void sendExportAlertEmail(String message) {
        // Meh.
    }

    @Override
    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Meh.
    }
}
