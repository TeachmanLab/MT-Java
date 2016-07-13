package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.service.EmailService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public void sendGiftCard(Participant participant, Reward reward, int amountCents) throws MessagingException {
        // Meh.
    }

    @Override
    public void sendAtRiskAlertToAdmin(Participant participant, String details) {

    }

    @Override
    public void sendEmail(Participant participant, String type) {

    }

    @Override
    public Map<String, String> emailTypes() {
        Map<String,String> emailTypes = new HashMap<>();
        emailTypes.put("day1Reminder", "Day One Reminder");
        emailTypes.put("day2Reminder", "Day Two Reminder");
        emailTypes.put("day12Reminder", "Day Twelve Reminder");
        emailTypes.put("day30Reminder", "Day Thirty Reminder");
        return emailTypes;
    }
}
