package org.mindtrails.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an email sent to a participant or admin.
 */
@Data
public class ScheduledEvent {

    public enum SCHEDULE_TYPE {EVENT, INACTIVITY, SINCE_COMPLETION}
    public enum EVENT_TYPE {EMAIL, TEXT, UPDATE}

    private EVENT_TYPE eventType;
    private Participant participant;  // participant receiving this email, or that this email is about.

    private final String type;  // This type should correspond to a template in /resources/templates/emails/ if this is an email, otherwise, can be anything descriptive.
    private String studyExtension;  // If specific to a particular study extension
    private List<String> sessions = new ArrayList<>(); // The session(s) we are checking against, if not set, always applied.
    private List<Integer> days = new ArrayList<>(); // # of days to measure
    private SCHEDULE_TYPE scheduleType; // how to measure, either days of inactivity within session

                                  // or days since the session was completed.
    private boolean includeCalendarInvite = false;
    private boolean templateExists = true;  // Verify that the template for the email exists.


    public ScheduledEvent(EVENT_TYPE eventType, String type, String studyExtension,
                          List<String> sessions,
                          List<Integer> days,
                          SCHEDULE_TYPE scheduleType) {
        /* Constructs an email that is sent out on a specific schedule, based on inactivity
           or completion date.
         */
        this.eventType = eventType;
        this.type = type;
        this.studyExtension = studyExtension;
        if(sessions != null) { this.sessions = sessions; }
        if(days != null) { this.days = days; }
        this.scheduleType = scheduleType;
    }

    public String content() {
        return "none";
    }
}
