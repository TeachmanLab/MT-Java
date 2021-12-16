package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.OA;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.ForceSessionEvent;
import org.mindtrails.domain.Scheduled.MarkInactiveEvent;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

/**
 * This is now a more complex email service that can define emails be sent out using a number of different rules.
 */
@Service
public class R01EmailService extends EmailServiceImpl implements EmailService{

    private final String RISING_SCORE = "risingScore";

    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> events = super.emailTypes();
        List<String> core_sessions = Arrays.asList(R01Study.SECOND_SESSION, R01Study.THIRD_SESSION,
                R01Study.FOURTH_SESSION, R01Study.FIFTH_SESSION);


        events.add(new Email("risingScore", "MindTrails Alert! Participant Score Is Rising"));

        // Bonus feature emails that come at the end of a session.
        events.add(new Email("SESSION1", "Bonus Feature #1 from the MindTrails Project Team",
                null, R01Study.FIRST_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team",
                null, R01Study.SECOND_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION3", "Bonus Feature #3 from the MindTrails Project Team",
                null, R01Study.THIRD_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));
        events.add(new Email("SESSION4", "Bonus Feature #4 from the MindTrails Project Team",
                null, R01Study.FOURTH_SESSION, 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));

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
        events.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, Arrays.asList(63, 70), Email.SCHEDULE_TYPE.INACTIVITY, false));

        // TET/GIDI Reminders
        events.add(new Email("headsup1", "The MindTrails Two Month Follow-Up Survey is Just 2 Weeks Away!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 46, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup", "It’s Time to Complete the MindTrails Two Month Follow-Up Survey!",
                null, R01Study.POST_FOLLOWUP, 60, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 63, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup2", "We’d love to hear from you!",
                null, R01Study.POST_FOLLOWUP, 67, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 70, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup3", "We’d love to help!",
                null, R01Study.POST_FOLLOWUP, 75, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("debrief", "Explanation of the MindTrails Study",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 120, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Close TET account after 120 days of inactivity on the POST_FOLLOWUP
        events.add(new MarkInactiveEvent("markInactive", R01Study.STUDY_EXTENSIONS.TET.name(),
                Arrays.asList(R01Study.POST_FOLLOWUP),
                120, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));

        // Force GIDI to POST_FOLLOWUP2 120 days after completing session 5.
        // ********************* Set it to GIDI, and set last activity to 120 days ago.
        events.add(new ForceSessionEvent("forceSession", R01Study.POST_FOLLOWUP2, 60, R01Study.STUDY_EXTENSIONS.GIDI.name(), Arrays.asList(R01Study.FIFTH_SESSION),
                120, ScheduledEvent.SCHEDULE_TYPE.SINCE_COMPLETION));

        events.add(new Email("headsup2", "The MindTrails Six Month Follow-Up Survey is Just 2 Weeks Away!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 106, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup_6month", "It’s Time to Complete the MindTrails Six Month Follow-Up Survey!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 120, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup2_6month", "Time to Complete the MindTrails Six-Month Follow-Up ($15)!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 127, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("followup3_6month", "There’s Still Time to Complete the MindTrails Six-Month Follow-Up Survey ($15)!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 134, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // GIDI Newsletter events
        events.add(new Email("newsletter1", "MindTrails Newsletter #1",

                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.FIRST_SESSION, 2, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));
        events.add(new Email("newsletter2", "MindTrails Newsletter #2",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 7, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));
        events.add(new Email("newsletter3", "MindTrails Newsletter #3",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 30, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));
        events.add(new Email("newsletter4", "MindTrails Newsletter #4",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 90, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));
        events.add(new Email("newsletter5", "MindTrails Newsletter #5",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 120, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));
        events.add(new Email("newsletter6", "MindTrails Newsletter #6",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 150, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));

        // Check that all the emails exist, and set this on the Email object.
        for(ScheduledEvent event: events) {
            if (event instanceof Email) {
                event.setTemplateExists(this.emailTemplateExists(event.getType()));
            }
        }

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
