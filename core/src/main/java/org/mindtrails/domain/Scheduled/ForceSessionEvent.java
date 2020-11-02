package org.mindtrails.domain.Scheduled;

import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.TwilioService;

import java.util.Collections;
import java.util.List;

/**
 * Forces the user to a specific session, useful for when you need to skip over
 * a sessions
 */
@Data
public class ForceSessionEvent extends ScheduledEvent {

    private String toSession;

    public ForceSessionEvent(String type, String toSession, String studyExtension,
                       List<String> sessions, int days, SCHEDULE_TYPE scheduleType) {
        super(EVENT_TYPE.FORCE_SESSION, type, studyExtension, sessions, Collections.singletonList(days), scheduleType);
        this.toSession = toSession;
    }


    public void execute(Participant p) {
        p.getStudy().forceToSession(toSession);
    }


    @Override
    public String getDescription() {
        return "Force user to " + toSession;
    }

    @Override
    public void execute(Participant p, EmailService emailService,
                        TwilioService twilioService) {
        p.getStudy().forceToSession(toSession);
    }
}
