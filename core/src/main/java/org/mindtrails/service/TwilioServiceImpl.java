package org.mindtrails.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.TextMessage;
import org.mindtrails.domain.tracking.SMSLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${server.url}")
    protected String serverUrl;



    public void sendMessage(TextMessage textMessage, Participant participant)  {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        SMSLog log = new SMSLog(participant, textMessage.getContent());
        try {
            Message message = Message
                    .creator(new PhoneNumber(participant.getPhone()),  // to
                            new PhoneNumber(PHONE_NUMBER),  // from
                            textMessage.getContent())
                    .create();
        } catch(ApiException apiError) {
            LOGGER.error("Failed to send SMS message:" + apiError.getLocalizedMessage());
            log.setError(apiError);
        } finally {
            participant.addSMSLog(log);
            participantRepository.save(participant);
        }
    }

}