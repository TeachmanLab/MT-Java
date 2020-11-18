package org.mindtrails.domain.Scheduled;

import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.TwilioService;

import java.util.Collections;
import java.util.List;

/**
 * Marks a participant as inactive.
 */
@Data
public class MarkInactiveEvent extends ScheduledEvent {

    public MarkInactiveEvent(String type, String studyExtension,
                             List<String> sessions, int days,
                             SCHEDULE_TYPE scheduleType) {
        super(EVENT_TYPE.MARK_INACTIVE, type, studyExtension, sessions, Collections.singletonList(days), scheduleType);
    }

    @Override
    public String getDescription() {
        return "Marks user as inactive";
    }

    @Override
    public void execute(Participant p, EmailService emailService,
                        TwilioService twilioService) {
        p.setActive(false);
    }

}
