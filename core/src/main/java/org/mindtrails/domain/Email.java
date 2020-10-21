package org.mindtrails.domain;

import lombok.Data;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents an email sent to a participant or admin.
 */
@Data
public class Email {

    public enum SCHEDULE_TYPE {EVENT, INACTIVITY, SINCE_COMPLETION}


    private final String type;  // This type should correspond to a template in /resources/templates/emails/
    private String subject;
    private String to;  // a valid email address
    private Context context; // Contextural data for the template, if needed.
    private Participant participant;  // participant receiving this email, or that this email is about.
    private Date calendarDate;


    private String studyExtension;  // If specific to a particular study extension
    private List<String> sessions = new ArrayList<>(); // The session(s) we are checking against, if not set, always applied.
    private List<Integer> days = new ArrayList<>(); // # of days to measure
    private SCHEDULE_TYPE scheduleType; // how to measure, either days of inactivity within session
                                  // or days since the session was completed.
    private boolean includeCalendarInvite = false;


    public Email (String type, String subject) {
        /* Creates an event based email, that can be sent on demand at any time, but is not scheduled */
        this.type = type;
        this.subject = subject;
        this.scheduleType = SCHEDULE_TYPE.EVENT;
    }


    public Email (String type, String subject, String studyExtension,
                  List<String> sessions, int days, SCHEDULE_TYPE measureType,
                  boolean includeCalendarInvite) {
        /* Constructs an email that is sent out on a specific schedule, based on inactivity
           or completion date.
         */
        this.type = type;
        this.subject = subject;
        this.studyExtension = studyExtension;
        this.sessions = sessions;
        this.days = Collections.singletonList(days);
        this.scheduleType = measureType;
        this.includeCalendarInvite = includeCalendarInvite;
    }


    public Email (String type, String subject, String studyExtension,
                  String session, int days, SCHEDULE_TYPE measureType,
                  boolean includeCalendarInvite) {
        this.type = type;
        this.subject = subject;
        this.studyExtension = studyExtension;
        this.sessions = Collections.singletonList(session);
        this.days = Collections.singletonList(days);
        this.scheduleType = measureType;
        this.includeCalendarInvite = includeCalendarInvite;
    }


    public Email (String type, String subject, String studyExtension,
                  String session, List<Integer> days, SCHEDULE_TYPE measureType,
                  boolean includeCalendarInvite) {
        this.type = type;
        this.subject = subject;
        this.studyExtension = studyExtension;
        this.sessions = Collections.singletonList(session);
        this.days = days;
        this.scheduleType = measureType;
        this.includeCalendarInvite = includeCalendarInvite;
    }


}
