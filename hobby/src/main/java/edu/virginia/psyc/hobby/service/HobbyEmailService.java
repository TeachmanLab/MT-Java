package edu.virginia.psyc.hobby.service;

import edu.virginia.psyc.hobby.domain.HobbyStudy;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Session;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * A basic email service that behcaves exactly like the core configuration,
 * sending email reminders each day at 2 am and providing endpoints for
 * dealing with standard email messages such as resetting passwords.
 */
@Service
public class HobbyEmailService extends EmailServiceImpl implements EmailService{
    private final String FIRST_SESSION = "SESSION1";
    private final String SECOND_SESSION = "SESSION2";

    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> emails = super.emailTypes();
        emails.add(new Email(FIRST_SESSION, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SECOND_SESSION, "Bonus feature from the MindTrails team"));
        return emails;
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;
        if (currentSession.getName().equals(HobbyStudy.FIRST_SESSION)) {
            email = new Email("SESSION1","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(HobbyStudy.SECOND_SESSION)) {
            email = new Email("SESSION2","Bonus feature from the MindTrails team");
        }
        if (email != null) {
            email.setTo(participant.getEmail());
            email.setParticipant(participant);
            email.setContext(new Context());
            sendEmail(email);
        }
    }
}
