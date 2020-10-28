package org.mindtrails.service;


import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.mindtrails.domain.*;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.EmailLogRepository;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.SMSLogRepository;
import org.mindtrails.persistence.TaskLogRepository;
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
    @Scheduled(cron = "0 */30 * * * *")  // this runs every 30 minutes
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, noRollbackFor=Exception.class)
    public void processEvents() {

        List<Participant> participants = participantRepository.findByActive(true);

        for (Participant participant : participants) {
            if (!timeToSendMessage(participant)) continue;
            List<ScheduledEvent> events = getEventsForParticipant(participant);
            for (ScheduledEvent event : events) {
                if (event instanceof Email) {
                    twilioService.sendMessage((TextMessage) event, participant);
                } else if (event instanceof TextMessage) {
                    emailService.sendEmail((Email) event);
                }
                // todo: Handle and log errors
                // todo: HANDLE Closure event and other non-notifiactions, mark as inactive
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

        // Has this event already caused a notification to the particpant in the past?
        if (alreadySentToParticipant(event, participant)) return false;

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

    protected boolean alreadySentToParticipant(ScheduledEvent event, Participant participant) {
        // Has this event already occurred in the past?
        if (event instanceof Email) {
            if (emailLogRepository.countByEmailTypeAndParticipant(event.getType(), participant) > 0) return true;
        }
        if (event instanceof TextMessage) {
            if (smsLogRepository.countByTypeAndParticipant(event.getType(), participant) > 0) return true;
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
        // This is 30 minute interval around the users current time.
        Interval interval = new Interval(
                new DateTime(DateTimeZone.forID(participant.getTimezone())).minusMinutes(15),
                new DateTime(DateTimeZone.forID(participant.getTimezone())).plusMinutes(15));

        // The time today when we should send a text message. (typically around 5pm)
        DateTime timeToSend = new DateTime(DateTimeZone.forID(participant.getTimezone()))
                .withTime(notifyHour, notifyMinute, 0, 0);
        if(interval.contains(timeToSend)) {
            return true;
        } else {
            return false;
        }
    }


}
