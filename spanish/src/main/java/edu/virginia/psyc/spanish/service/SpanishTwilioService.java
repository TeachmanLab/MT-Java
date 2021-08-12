package edu.virginia.psyc.spanish.service;


import edu.virginia.psyc.spanish.domain.SpanishStudy;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpanishTwilioService extends TwilioServiceImpl implements TwilioService {
    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList(
                SpanishStudy.SECOND_SESSION);


        messages.add(new TextMessage("7day",
                "text.7day" + serverUrl,
                null, core_sessions, 7, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("10day",
                "text.10day" + serverUrl,
                null, core_sessions, 10, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("14day",
                "text.14day" + serverUrl,
                null, core_sessions, 14, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("18day",
                "text.18day" + serverUrl,
                null, core_sessions, 18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

//        messages.add(new TextMessage("21day",
//                "MindTrails account closed. Would you mind completing a quick survey?"
//                        + serverUrl + "'/questions/ReasonsForEnding'",
//                null, core_sessions, 21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        return messages;
    }
}

