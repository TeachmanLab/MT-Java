package edu.virginia.psyc.templeton;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Reward;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dan on 5/13/16.
 */
@Service
public class EmailService implements org.mindtrails.service.EmailService {

    @Override
    public void sendExportAlertEmail(String message) {
        // Meh.
    }

    @Override
    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Meh.
    }

    @Override
    public void sendGiftCard(Participant participant, Reward reward, int amountCents) throws MessagingException {
        // Meh.
    }


    @Override
    public void sendEmail(Participant participant, String type) {

    }

    @Override
    public Map<String, String> emailTypes() {
        Map<String,String> emailTypes = new HashMap<>();
        emailTypes.put("email1", "First Email");
        return emailTypes;
    }




}
