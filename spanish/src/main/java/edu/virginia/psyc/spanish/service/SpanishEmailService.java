package edu.virginia.psyc.spanish.service;

import edu.virginia.psyc.spanish.domain.SpanishStudy;
import edu.virginia.psyc.spanish.persistence.OA;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.MarkInactiveEvent;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A basic email service that behcaves exactly like the core configuration,
 * sending email reminders each day at 2 am and providing endpoints for
 * dealing with standard email messages such as resetting passwords.
 */
@Service
public class SpanishEmailService extends EmailServiceImpl implements EmailService{

    @Autowired
    protected MessageSource messageSource;

    private String RISING_SCORE = "risingScore";

    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> events = new ArrayList<>();  // Use an empty list, and override all subject headers below.
        List<String> core_sessions = Arrays.asList(SpanishStudy.SECOND_SESSION);

        events.add(new Email("alertAdmin", "MindTrails Alert!"));
        events.add(new Email("risingScore", "MindTrails Alert! Participant Score Is Rising"));

        events.add(new Email("resetPass", "reset.subject"));
        events.add(new Email("giftCard", "reset.gift"));
        events.add(new Email("midSessionStop", "stop.subject"));

        // Bonus feature emails that come at the end of a session.
        events.add(new Email("SESSION1", "bonus.subject",
                null, SpanishStudy.FIRST_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        /*
        events.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team",
                null, SpanishStudy.SECOND_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        */
        // Reminder emails when users are inactive longer than they should be in the core sessions (2,3,4,5)
        events.add(new Email("day7", "reminder.subject",
                null, SpanishStudy.FIRST_SESSION, 7, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day10", "reminder.subject",
                null, core_sessions, 10, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day14", "reminder.subject",
                null, core_sessions, 14, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day18", "reminder.subject",
                null, core_sessions, 18, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("closure", "closure.subject",
                null, core_sessions, 21, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Close accounts after 21 days of inactivity in core sessions
        events.add(new MarkInactiveEvent("markInactive", null, core_sessions,
                21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));

        return events;
    }


    public String translate(String code, Participant p) {
        Object[] args = new String[0];
        try {
            return this.messageSource.getMessage(code, args, p.locale());
        } catch (Exception e) {
            return code;
        }
    }

    public void sendAtRiskAdminEmail(Participant participant, OA firstEntry, OA currentEntry)  {

        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("orig", firstEntry);
        ctx.setVariable("latest", currentEntry);

        Email email = getEmailForType(RISING_SCORE);
        email.setContext(ctx);
        email.setTo(this.alertsTo);
        email.setParticipant(participant);

        sendEmail(email);
    }

    @Override
    public void sendEmail(Email email) {
        if (email.getParticipant() != null) {
            email.setSubject(translate(email.getSubject(), email.getParticipant()));
        }
        super.sendEmail(email);
    }

}
