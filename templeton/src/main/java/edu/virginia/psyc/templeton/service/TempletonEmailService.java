package edu.virginia.psyc.templeton.service;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

/**
 * A basic email service that behcaves exactly like the core configuration,
 * sending email reminders each day at 2 am and providing endpoints for
 * dealing with standard email messages such as resetting passwords.
 */
@Service
public class TempletonEmailService extends EmailServiceImpl implements EmailService{
    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;
        if (currentSession.getName().equals(TempletonStudy.PRE_TEST.toString())) {
            email = new Email("preTest","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(TempletonStudy.FIRST_SESSION.toString())) {
            email = new Email("firstSession","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(TempletonStudy.SECOND_SESSION.toString())) {
            email = new Email("secondSession","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(TempletonStudy.THIRD_SESSION.toString())) {
            email = new Email("thirdSession","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(TempletonStudy.FOURTH_SESSION.toString())) {
            email = new Email("fourthSession","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(TempletonStudy.POST_FOLLOWUP.toString())) {
            email = new Email("postFollowUp","Bonus feature from the MindTrails team");
        }
        if (email != null) {
            email.setTo(participant.getEmail());
            email.setParticipant(participant);
            email.setContext(new Context());
            sendEmail(email);
        }
    }
}
