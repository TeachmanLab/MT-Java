package org.mindtrails.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Sends and recieves Text Messages through the Trilio Service.
 * You will need a Trilio account and phone number.
 *
 */
@Data
@Service
public class TwilioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioService.class);

    // Find your Account Sid and Token at twilio.com/user/account
    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone_number}")
    private String PHONE_NUMBER;


    public void sendMessage()  {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            Message message = Message
                    .creator(new PhoneNumber("+15404570024"),  // to
                            new PhoneNumber(PHONE_NUMBER),  // from
                            "Time for your next session on MindTrails!  Please visit https://mindtrails.virginia.edu to get started.")
                    .create();
        } catch(ApiException apiError) {
            LOGGER.error("Failed to send SMS message:" + apiError.getLocalizedMessage());
        }
    }

    /*
    @Scheduled(cron = "0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendTextReminder() {
        List<Participant> participants;

        participants = participantRepository.findAll();
        TYPE type;

        for(Participant participant : participants) {
            type = getTypeToSend(participant);
            if(type != null) {
                Email email = getEmailForType(type.toString());
                email.setTo(participant.getEmail());
                email.setParticipant(participant);
                email.setContext(new Context());
                sendEmail(email);
            }
        }
    }
    */
}