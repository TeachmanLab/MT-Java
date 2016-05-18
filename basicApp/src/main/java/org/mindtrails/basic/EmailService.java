package org.mindtrails.basic;

import edu.virginia.psyc.mindtrails.domain.Participant;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * Created by dan on 5/13/16.
 */
@Service
public class EmailService implements edu.virginia.psyc.mindtrails.service.EmailService {

    @Override
    public void sendExportAlertEmail(String message) {
        // Meh.
    }

    @Override
    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Meh.
    }


}
