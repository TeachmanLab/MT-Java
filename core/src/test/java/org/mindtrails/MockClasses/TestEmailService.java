package org.mindtrails.MockClasses;

import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.ForceSessionEvent;
import org.mindtrails.domain.Scheduled.MarkInactiveEvent;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TestEmailService extends EmailServiceImpl implements EmailService {
    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> events = super.emailTypes();
        List<String> core_sessions = Arrays.asList("SessionOne", "SessionTwo");

        // Reminder events when users are inactive longer than they should be in the core sessions (2,3,4,5)
        events.add(new Email("gidiOnly", "a Gidi Only Email",
                "gidi", core_sessions, 1, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Email that is delivered based on the date a session was completed.
        events.add(new Email("afterCompleted", "An Email that goes out 5 days after session one is done.",
                null, "SessionOne", 5, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));

        // Email that contains a calendar invite for the next session.
        events.add(new Email("day0", "A day 0 Email with Calendar Invitation",
                null, "SessionOne", 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));

        events.add(new Email("day2", "A day 2 email",
                null, core_sessions, 2, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day4", "Update from the MindTrails Project Team",
                null, core_sessions, 4, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day7", "Update from the MindTrails Project Team",
                null, core_sessions, 7, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day11", "Update from the MindTrails Project Team",
                null, core_sessions, 11, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("day15", "Update from the MindTrails Project Team",
                null, core_sessions, 15, Email.SCHEDULE_TYPE.INACTIVITY, false));
        events.add(new Email("closure", "Important Reminder from the MindTrails Project Team",
                null, core_sessions, 18, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Add two additional events that will either mark accounts as inactive, or force the user
        // to a specifc session.
        events.add(new MarkInactiveEvent("markInactive", "tet",
                Arrays.asList("SessionOne"),
                18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));

        events.add(new ForceSessionEvent("forceSession", "PostSession", "gidi", Arrays.asList("SessionOne"),
                18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY));


        events.add(new Email("followup", "Update from the MindTrails Project Team",
                null, "PostSession", 60, Email.SCHEDULE_TYPE.INACTIVITY, false));


        return events;
    }
}
