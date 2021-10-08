package edu.virginia.psyc.spanish.service;


import edu.virginia.psyc.spanish.domain.SpanishStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpanishTwilioService extends TwilioServiceImpl implements TwilioService {

    @Autowired
    protected MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(SpanishTwilioService.class);

    public void sendMessage(TextMessage textMessage, Participant participant)  {

        String content = translate(textMessage.getContent(), participant);
        content = content + " " + this.serverUrl + "login";
        if(participant.getStudy().getConditioning().contains("SPANISH")) {
            content = content + "?lang=es";
        }
        textMessage.setContent(content);

        super.sendMessage(textMessage, participant);
    }

    public String translate(String code, Participant p) {
        Object[] args = new String[0];
        try {
            return this.messageSource.getMessage(code, args, p.locale());
        } catch (Exception e) {
            return code;
        }
    }

    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList(
                SpanishStudy.SECOND_SESSION);

        messages.add(new TextMessage("7day",
                "text.7day",
                null, core_sessions, 7, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));

        messages.add(new TextMessage("10day",
                "text.10day",
                null, core_sessions, 10, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));

        messages.add(new TextMessage("14day",
                "text.14day",
                null, core_sessions, 14, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));

        messages.add(new TextMessage("18day",
                "text.18day",
                null, core_sessions, 18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));

//        messages.add(new TextMessage("21day",
//                "MindTrails account closed. Would you mind completing a quick survey?"
//                        + serverUrl + "'/questions/ReasonsForEnding'",
//                null, core_sessions, 21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        return messages;
    }
}

