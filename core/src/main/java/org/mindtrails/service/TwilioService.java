package org.mindtrails.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.mindtrails.domain.ExportMode;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tracking.SMSLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sends and recieves Text Messages through the Trilio Service.
 * You will need a Trilio account and phone number.
 *
 */
@Data
@Service
public class TwilioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioService.class);


    @Autowired
    protected ParticipantRepository participantRepository;

    // Find your Account Sid and Token at twilio.com/user/account
    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone_number}")
    private String PHONE_NUMBER;

    @Value("${twilio.notify_hour}")
    private int notifyHour;

    @Value("${twilio.notify_minute}")
    private int notifyMinute;

    @Value("${server.url}")
    private String serverUrl;



    public void sendMessage(String textMessage, Participant participant)  {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        SMSLog log = new SMSLog(participant, textMessage);
        try {
            Message message = Message
                    .creator(new PhoneNumber(participant.getPhone()),  // to
                            new PhoneNumber(PHONE_NUMBER),  // from
                            textMessage)
                    .create();
        } catch(ApiException apiError) {
            LOGGER.error("Failed to send SMS message:" + apiError.getLocalizedMessage());
            log.setError(apiError);
        } finally {
            participant.addSMSLog(log);
            participantRepository.save(participant);
        }
    }

    public boolean timeToSendMessage(Participant participant) {
        // This is 30 minute interval around the users current time.
        Interval interval = new Interval(
                new DateTime(DateTimeZone.forID(participant.getTimezone())).minusMinutes(15),
                new DateTime(DateTimeZone.forID(participant.getTimezone())).plusMinutes(15));

        // The time today when we should send a text message.
        DateTime timeToSend = new DateTime(DateTimeZone.forID(participant.getTimezone()))
                .withTime(notifyHour, notifyMinute, 0, 0);

        if(interval.contains(timeToSend)) {
            return true;
        } else {
            return false;
        }
    }

    @ExportMode
    @Scheduled(cron = "0 */30 * * * *")  // evey 30 minutes
    public void sendTextReminder() {
        List<Participant> participants;
        String message;

        participants = participantRepository.findByActiveAndPhoneReminders(true, true);

        for(Participant participant : participants) {

            // If there is no message for this participant,
            // or it's not the right time of day, then continue.
            message = getMessage(participant);
            if(message.equals("")) continue;
            if(timeToSendMessage(participant) == false) continue;

            // Otherwise, send them a text message.
            sendMessage(message, participant);
        }
    }

    /**
     * Given a participant, determines which message to send that
     * participant.  If no message should be sent, returns Null.
     * @param p
     * @return
     */
    public String getMessage(Participant p) {

        String message = "";

        // Never send more than one text message a day.
        if (p.daysSinceLastSMSMessage() < 2) return "";

        // Never send email to an inactive participant;
        if (!p.isActive()) return "";

        int days = p.daysSinceLastMilestone();
        Study study = p.getStudy();
        Session session = study.getCurrentSession();

        // If they are waiting for 2 days, then remind them
        // at the end of 2 days, then again after 4,7,11,15, and 18
        // days since their last session.
        if(session.getDaysToWait() <= 2) {
            switch (days) {
                case 2:
                    message = "Time to start your next MindTrails Session!  Visit :" + serverUrl;
                    break;
                case 4:
                    message = "Complete your next MindTrails session soon!  Visit :" + serverUrl;
                    break;
                case 7:
                    message = "It has been a week since your last MindTrails Session.  Start Now :" + serverUrl;
                    break;
                case 11:
                    message = "Still interested in MindTrails?  Catch back up here:" + serverUrl;
                    break;
                case 15:
                    message = "Final reminder about your MindTrails Account!  You can start back up here:" + serverUrl;
                    break;
                case 18:
                    message = "MindTrails account closed. Would you mind completing a quick survey?"
                            + serverUrl + "'/questions/ReasonsForEnding'";
                    break;
            }
        }

        // Follow up emails are sent out for tasks for delays of
        // 60 days or more.
        if(session.getDaysToWait() >= 60) {
            switch (days) {
                case 60:
                    message = "Time to complete the final survey on MindTrails: " + serverUrl;
                    break;
                case 63:
                    message = "Final MindTrails Survey is ready: " + serverUrl;
                    break;
                case 67:
                    message = "Final MindTrails Survey is ready: " + serverUrl;
                    break;
                case 70:
                    message = "Final MindTrails Survey is ready: " + serverUrl;
                    break;
                case 75:
                    message = "Final Request to please complete a final MindTrails survey: " + serverUrl;
                    break;
            }
        }
        return message;
    }

}