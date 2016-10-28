package edu.virginia.psyc.mobile;

import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Reward;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Diheng on 8/29/16.
 */
@Service
public class EmailService implements org.mindtrails.service.EmailService{


    @Override
    public void sendAdminEmail(String subject, String message) {

    }

    @Override
    public void sendPasswordReset(Participant participant) {

    }

    @Override
    public void sendGiftCard(Participant participant, Reward reward, int amountCents) {

    }

    @Override
    public void sendEmail(Email email) {

    }

    @Override
    public List<Email> emailTypes() {
        return null;
    }

    @Override
    public void sendExample(Email email) {

    }

    @Override
    public Email getEmailForType(String type) {
        return null;
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        return;
    }
}
