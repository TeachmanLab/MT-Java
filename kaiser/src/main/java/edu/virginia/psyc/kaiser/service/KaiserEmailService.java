package edu.virginia.psyc.kaiser.service;

import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import edu.virginia.psyc.kaiser.persistence.OA;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.MarkInactiveEvent;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

/**
 * A basic email service that behcaves exactly like the core configuration,
 * sending email reminders each day at 2 am and providing endpoints for
 * dealing with standard email messages such as resetting passwords.
 */
@Service
public class KaiserEmailService extends EmailServiceImpl implements EmailService{

    private String RISING_SCORE = "risingScore";

    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> events = super.emailTypes();
        List<String> core_sessions = Arrays.asList(KaiserStudy.SECOND_SESSION, KaiserStudy.THIRD_SESSION,
                KaiserStudy.FOURTH_SESSION, KaiserStudy.FIFTH_SESSION);

        events.add(new Email("risingScore", "MindTrails Alert! Participant Score Is Rising"));
        // Bonus feature emails that come at the end of a session.
        events.add(new Email("SESSION1", "Bonus Feature #1 from the MindTrails Project Team",
                null, KaiserStudy.FIRST_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team",
                null, KaiserStudy.SECOND_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION3", "Bonus Feature #3 from the MindTrails Project Team",
                null, KaiserStudy.THIRD_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION4", "Bonus Feature #4 from the MindTrails Project Team",
                null, KaiserStudy.FOURTH_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));

        // Reminder emails when users are inactive longer than they should be in the core sessions (2,3,4,5)
        events.add(new Email("day7", "Update from the MindTrails Project Team",
                null, core_sessions, 7, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day10", "Update from the MindTrails Project Team",
                null, core_sessions, 10, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day14", "Update from the MindTrails Project Team",
                null, core_sessions, 14, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day18", "Important Reminder from the MindTrails Project Team",
                null, core_sessions, 18, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("closure", "Closure of Account in the MindTrails Study",
                null, core_sessions, 21, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Close accounts after 21 days of inactivity in core sessions
        events.add(new MarkInactiveEvent("markInactive", null, core_sessions,
                21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));

        // TET Only Notifications in Followup Session
        events.add(new Email("followup", "It’s Time to Complete the MindTrails Two Month Follow-Up Survey!",
                null, KaiserStudy.POST_FOLLOWUP, 60, Email.SCHEDULE_TYPE.INACTIVITY, false));

        events.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team",
                null, KaiserStudy.POST_FOLLOWUP, Arrays.asList(63, 70), Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup3", "We’d love to help!",
                null, KaiserStudy.POST_FOLLOWUP, 75, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Close TET account after 120 days of inactivity on the POST_FOLLOWUP
        events.add(new MarkInactiveEvent("markInactive", null, Arrays.asList(KaiserStudy.POST_FOLLOWUP),
                120, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));

        return events;
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

}
