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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Sends and recieves Text Messages through the Trilio Service.
 * You will need a Trilio account and phone number.
 *
 */
@Data
@Service
public abstract class TwilioServiceImpl implements TwilioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioServiceImpl.class);


    @Autowired
    protected ParticipantRepository participantRepository;

    // Find your Account Sid and Token at twilio.com/user/account
    @Value("${twilio.account_sid}")
    protected String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    protected String AUTH_TOKEN;

    @Value("${twilio.phone_number}")
    protected String PHONE_NUMBER;

    @Value("${twilio.notify_hour}")
    protected int notifyHour;

    @Value("${twilio.notify_minute}")
    protected int notifyMinute;

    @Value("${server.url}")
    protected String serverUrl;



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
    @Transactional(propagation=Propagation.REQUIRED, readOnly=false, noRollbackFor=Exception.class)
    public void sendTextReminder() {
        List<Participant> participants;
        String message;

        participants = participantRepository.findByActiveAndPhoneReminders(true, true);

        for(Participant participant : participants) {

            // If there is no message for this participant,
            // or it's not the right time of day, then continue.

            if(!participantShouldGetMessages(participant)) continue;
            message = getMessage(participant);
            if(message.equals("")) continue;
            if(timeToSendMessage(participant) == false) continue;

            // Otherwise, send them a text message.
            sendMessage(message, participant);
        }
    }

    public boolean participantShouldGetMessages(Participant p) {

        // Never send more than one text message a day.
        if (p.daysSinceLastSMSMessage() < 2) return false;

        // Never send email to an inactive participant;
        if (!p.isActive()) return false;

        if(!p.isPhoneReminders()) return false;

        return true;
    }

    /**
     * Given a participant, determines which message to send that
     * participant.  If no message should be sent, returns Null.
     * @param p
     * @return
     */
    public abstract String getMessage(Participant p);


}