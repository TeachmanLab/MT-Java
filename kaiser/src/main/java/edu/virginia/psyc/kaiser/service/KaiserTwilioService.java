package edu.virginia.psyc.kaiser.service;


import edu.virginia.psyc.kaiser.domain.KaiserStudy;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KaiserTwilioService extends TwilioServiceImpl implements TwilioService {
    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList(
                KaiserStudy.SECOND_SESSION, KaiserStudy.THIRD_SESSION,
                KaiserStudy.FOURTH_SESSION, KaiserStudy.FIFTH_SESSION);


        messages.add(new TextMessage("7day",
                "Time to start your next MindTrails Session! Visit :" + serverUrl,
                null, core_sessions, 7, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("10day",
                "Complete your next MindTrails session soon! Visit :" + serverUrl,
                null, core_sessions, 10, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("14day",
                "It has been two weeks since your last MindTrails session.  Start Now :" + serverUrl,
                null, core_sessions, 14, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("18day",
                "Final reminder about your MindTrails account!  You can start back up here:" + serverUrl,
                null, core_sessions, 18, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("21day",
                "MindTrails account closed. Would you mind completing a quick survey?"
                        + serverUrl + "'/questions/ReasonsForEnding'",
                null, core_sessions, 21, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("60day",
                "The MindTrails 2 month follow-up is ready now! Follow this link to complete: " + serverUrl,
                null, KaiserStudy.POST_FOLLOWUP, 60, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("63day",
                "Final MindTrails survey is ready: " + serverUrl,
                null, KaiserStudy.POST_FOLLOWUP, 63, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("67day",
                "Time to complete the MindTrails 2 month follow-up! Follow this link to complete: " + serverUrl,
                null, KaiserStudy.POST_FOLLOWUP, 67, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("70day",
                "Final MindTrails survey is ready: " + serverUrl,
                null, KaiserStudy.POST_FOLLOWUP, 70, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("75day",
                "Time to complete the MindTrails 2 month follow-up! Follow this link to complete: " + serverUrl,
                null, KaiserStudy.POST_FOLLOWUP, 75, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        return messages;
    }
}

