package org.mindtrails.domain.Scheduled;

import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.TwilioService;

import java.util.Collections;
import java.util.List;

/**
 * Represents an text message sent to a participant, possibly based on a pre-defined schedule.
 */
@Data
public class TextMessage extends ScheduledEvent {

    private String content;
    private Boolean onlyIfNoEmails;


    public TextMessage(String type, String content) {
        super(EVENT_TYPE.TEXT,"immediate", null, null, Collections.singletonList(0), SCHEDULE_TYPE.EVENT);
        this.content = content;
        this.onlyIfNoEmails = false;
    }

    public TextMessage(String type, String content, String studyExtension,
                       List<String> sessions, int days, SCHEDULE_TYPE scheduleType,
                       Boolean onlyIfNoEmails) {
        super(EVENT_TYPE.TEXT, type, studyExtension, sessions, Collections.singletonList(days), scheduleType);
        this.content = content;
        this.onlyIfNoEmails = onlyIfNoEmails;
    }


    public TextMessage (String type, String content, String studyExtension,
                  String session, int days, SCHEDULE_TYPE scheduleType,
                        Boolean onlyIfNoEmails) {
        super(EVENT_TYPE.TEXT, type, studyExtension, Collections.singletonList(session), Collections.singletonList(days), scheduleType);
        this.content = content;
        this.onlyIfNoEmails = onlyIfNoEmails;
    }

    @Override
    public String getDescription() {
        return content;
    }

    @Override
    public void execute(Participant p, EmailService emailService,
                        TwilioService twilioService) {
        twilioService.sendMessage(this, p);
    }


}
