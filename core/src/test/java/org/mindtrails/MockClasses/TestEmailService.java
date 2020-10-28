package org.mindtrails.MockClasses;

import org.mindtrails.domain.Email;
import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TestEmailService extends EmailServiceImpl implements EmailService {
    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> emails = super.emailTypes();
        List<String> core_sessions = Arrays.asList("SessionOne", "SessionTwo");

        // Reminder emails when users are inactive longer than they should be in the core sessions (2,3,4,5)
        emails.add(new Email("gidiOnly", "a Gidi Only Email",
                "gidi", core_sessions, 1, Email.SCHEDULE_TYPE.INACTIVITY, false));

        // Email that is delivered based on the date a session was completed.
        emails.add(new Email("afterCompleted", "An Email that goes out 5 days after session one is done.",
                null, "SessionOne", 5, Email.SCHEDULE_TYPE.SINCE_COMPLETION, false));

        // Email that contains a calendar invite for the next session.
        emails.add(new Email("day0", "A day 0 Email with Calendar Invitation",
                null, "SessionOne", 0, Email.SCHEDULE_TYPE.SINCE_COMPLETION, true));

        emails.add(new Email("day2", "A day 2 email",
                null, core_sessions, 2, Email.SCHEDULE_TYPE.INACTIVITY, false));
        emails.add(new Email("day4", "Update from the MindTrails Project Team",
                null, core_sessions, 4, Email.SCHEDULE_TYPE.INACTIVITY, false));
        emails.add(new Email("day7", "Update from the MindTrails Project Team",
                null, core_sessions, 7, Email.SCHEDULE_TYPE.INACTIVITY, false));
        emails.add(new Email("day11", "Update from the MindTrails Project Team",
                null, core_sessions, 11, Email.SCHEDULE_TYPE.INACTIVITY, false));
        emails.add(new Email("day15", "Update from the MindTrails Project Team",
                null, core_sessions, 15, Email.SCHEDULE_TYPE.INACTIVITY, false));
        emails.add(new Email("closure", "Important Reminder from the MindTrails Project Team",
                null, core_sessions, 18, Email.SCHEDULE_TYPE.INACTIVITY, false));

        emails.add(new Email("followup", "Update from the MindTrails Project Team",
                null, "PostSession", 60, Email.SCHEDULE_TYPE.INACTIVITY, false));


        return emails;
    }
}
