package org.mindtrails.domain;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * Represents an email sent to a participant or admin.
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

    public String content() {
        return content;
    }

}
