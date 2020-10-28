package org.mindtrails.domain;

import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents an email sent to a participant or admin.
 */
@Data
public class Email extends ScheduledEvent {

    private String subject;
    private String to;  // a valid email address
    private Context context; // Contextural data for the template, if needed.
    private Participant participant;  // participant receiving this email, or that this email is about.
    private Date calendarDate;
    private boolean includeCalendarInvite = false;
    private boolean templateExists = false;  // Verify that the template for the email exists.



    public Email (String type, String subject) {
        /* Creates an event based email, that can be sent on demand at any time, but is not scheduled */
        super(EVENT_TYPE.EMAIL, type, null, null, Collections.singletonList(0), ScheduledEvent.SCHEDULE_TYPE.EVENT);
        this.subject = subject;
    }


    public Email (String type, String subject, String studyExtension,
                  List<String> sessions, int days, SCHEDULE_TYPE scheduleType,
                  boolean includeCalendarInvite) {
        /* Constructs an email that is sent out on a specific schedule, based on inactivity
           or completion date.
         */
        super(EVENT_TYPE.EMAIL, type, studyExtension, sessions, Collections.singletonList(days), scheduleType);
        this.subject = subject;
        this.includeCalendarInvite = includeCalendarInvite;
    }


    public Email (String type, String subject, String studyExtension,
                  String session, int days, SCHEDULE_TYPE scheduleType,
                  boolean includeCalendarInvite) {
        super(EVENT_TYPE.EMAIL, type, studyExtension, Collections.singletonList(session),
                Collections.singletonList(days), scheduleType);
        this.subject = subject;
        this.includeCalendarInvite = includeCalendarInvite;
    }


    public Email (String type, String subject, String studyExtension,
                  String session, List<Integer> days, SCHEDULE_TYPE scheduleType,
                  boolean includeCalendarInvite) {
        super(EVENT_TYPE.EMAIL, type, studyExtension, Collections.singletonList(session), days, scheduleType);
        this.subject = subject;
        this.includeCalendarInvite = includeCalendarInvite;
    }

    public String content() {
        return subject;
    }

}
