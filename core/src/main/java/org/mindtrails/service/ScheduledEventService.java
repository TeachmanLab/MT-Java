package org.mindtrails.service;


import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.mindtrails.domain.*;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.EmailLogRepository;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.SMSLogRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all notifications that go out of the system on a regular schedule
 */
@Data
@Service
public class ScheduledEventService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledEventService.class);


    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected TwilioService twilioService;

    @Autowired
    protected TaskLogRepository taskLogRepository;

    @Autowired
    protected EmailLogRepository emailLogRepository;

    @Autowired
    protected SMSLogRepository smsLogRepository;


    @Value("${twilio.notify_hour}")
    protected int notifyHour;

    @Value("${twilio.notify_minute}")
    protected int notifyMinute;



    public List<ScheduledEvent> getScheduledEvents() {
        List<ScheduledEvent> events = twilioService.messageTypes();
        events.addAll(emailService.emailTypes());
        return events;
    }


    @ExportMode
    @Scheduled(cron = "* 0/30 * * * *")  // this runs every 30 minutes
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, noRollbackFor=Exception.class)
    public void processEvents() {

        List<Participant> participants = participantRepository.findByActive(true);
        LOG.info("Running scheduled event service.");
        for (Participant participant : participants) {
            try {
                if (!timeToSendMessage(participant)) {
                    continue;
                }
                List<ScheduledEvent> events = getEventsForParticipant(participant);
                LOG.info("Checking events for Participant " + participant.getId() + " Total Events: " + events.size());
                for (ScheduledEvent event : events) {
                    event.execute(participant, emailService, twilioService);
                    participantRepository.save(participant);
                }
            } catch (Exception e) {
                LOG.info("Notifications failed for Participant " + participant.getId() + ".  " + e.getMessage());
            }
        }
    }

    /**
     * Returns a list of scheduled events that should occur for this participant now.
     */
    public List<ScheduledEvent> getEventsForParticipant(Participant participant) {
        List<ScheduledEvent> allEvents = getScheduledEvents();
        List<ScheduledEvent> currentEvents = new ArrayList<>();
        if(!participant.isActive()) return currentEvents;

        for (ScheduledEvent event : allEvents) {
            if (shouldExecuteEventForParticipant(event, participant)) {
                if (event instanceof Email) {
                    Email email = (Email) event;
                    if (participant.daysSinceLastEmail() < 2) continue;
                    if (!participant.isEmailReminders()) continue;
                    prepareEmail(email, participant);
                    currentEvents.add(event);
                } else if (event instanceof TextMessage) {
                    if (participant.daysSinceLastSMSMessage() < 2) continue;
                    TextMessage tm = (TextMessage)event;
                    if (tm.getOnlyIfNoEmails() && participant.isEmailReminders()) continue;
                    currentEvents.add(event);
                } else {
                    currentEvents.add(event);
                }
            }
        }
        return currentEvents;
    }


    private void prepareEmail(Email email, Participant participant) {
        email.setTo(participant.getEmail());
        email.setParticipant(participant);
        email.setContext(new Context());
        // If this email should include a calendar invite,
        // and the participant set a return date, set the date on the email
        if(email.isIncludeCalendarInvite() && participant.getReturnDate() != null) {
            email.setCalendarDate(participant.getReturnDate());
        }
    }


    public boolean shouldExecuteEventForParticipant(ScheduledEvent event, Participant participant) {

        // Check the study extension, if it is not null, it must match.
        if (event.getStudyExtension() != null &&
                !event.getStudyExtension().equals(participant.getStudy().getStudyExtension()))
            return false;

        // If this event is based on inactivity for the current session ....
        Session currentSession = participant.getStudy().getCurrentSession();
        if (event.getScheduleType().equals(ScheduledEvent.SCHEDULE_TYPE.INACTIVITY) &&
                event.getSessions().contains(currentSession.getName())) {
            if (event.getDays().contains(participant.daysSinceLastMilestone())) {
                return true;
            }
        }

        // If this event is based on days since completion of a prior session
        if (event.getScheduleType().equals(ScheduledEvent.SCHEDULE_TYPE.SINCE_COMPLETION)) {
            for (String sessionName : event.getSessions()) {
                int days = daysSinceCompletion(participant.getStudy(), sessionName);
                if (event.getDays().contains(days)) {
                    return true;
                }
            }
        }
        return false;
    }


    protected int daysSinceCompletion(Study study, String sessionName) {
        TaskLog log = taskLogRepository.findByStudyAndSessionNameAndTaskName(study, sessionName, TaskLog.SESSION_COMPLETE);
        if(log != null) {
        return Days.daysBetween(new DateTime(log.getDateCompleted()), new DateTime()).getDays();
        } else {
        return -1;
        }
    }


    /**
     * Returns true if the user's current time (in their timezone) is within 30 minutes
     * of our designated hour in which to send out messages.  Otherwise we don't send.
     * In this way our notifications always go out at a paticular time of day.  We've
     * historically set this to be around 5 pm each day.
     * @param participant
     * @return
     */
    public boolean timeToSendMessage(Participant participant) {

        // Try to pick up the user's timezone, but fall back to the default otherwise.
        String timezone = "America/New_York";
        if(participant.getTimezone() != null && DateTimeZone.getAvailableIDs().contains(participant.getTimezone())) {
            timezone = participant.getTimezone();
        } else {
            LOG.error("Participant #" + participant.getId() + " has an invalid timezone of " + participant.getTimezone());
        }

        // This is 1.5 hour interval around the users current time.
        Interval interval = new Interval(
                new DateTime(DateTimeZone.forID(timezone)).minusMinutes(45),
                new DateTime(DateTimeZone.forID(timezone)).plusMinutes(45));

        // The time today when we should send a text message. (typically around 5pm)
        DateTime timeToSend = new DateTime(DateTimeZone.forID(participant.getTimezone()))
                .withTime(notifyHour, notifyMinute, 0, 0);
        return interval.contains(timeToSend);
    }


}
