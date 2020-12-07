package org.mindtrails.domain.Scheduled;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.TwilioService;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Forces the user to a specific session, useful for when you need to skip over
 * a sessions
 */
@Data
public class ForceSessionEvent extends ScheduledEvent {

    private String toSession;
    private int inactivityDays; // Once moved to a session, sets the number of days of innactivity, default is 0

    public ForceSessionEvent(String type, String toSession, int inactivityDays, String studyExtension,
                       List<String> sessions, int days, SCHEDULE_TYPE scheduleType) {
        super(EVENT_TYPE.FORCE_SESSION, type, studyExtension, sessions, Collections.singletonList(days), scheduleType);
        this.toSession = toSession;
        this.inactivityDays = inactivityDays;
    }

    @Override
    public String getDescription() {
        return "Force user to " + toSession;
    }

    @Override
    public void execute(Participant p, EmailService emailService,
                        TwilioService twilioService) {
        p.getStudy().forceToSession(toSession);
        Date new_date = DateTime.now().minus(Days.days(inactivityDays)).toDate();
        p.getStudy().setLastSessionDate(new_date);
        p.setLastLoginDate(new_date);
        System.out.println(p.daysSinceLastMilestone());
    }
}
