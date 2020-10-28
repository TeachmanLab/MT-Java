package org.mindtrails.MockClasses;

import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.domain.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TestTwilioService extends TwilioServiceImpl implements TwilioService {

    @Override
    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList("SessionOne", "SessionTwo");


        messages.add(new TextMessage("7day",
                "Time to start your next MindTrails Session! Visit :" + serverUrl,
                null, core_sessions, 7, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        return messages;
    }
}
