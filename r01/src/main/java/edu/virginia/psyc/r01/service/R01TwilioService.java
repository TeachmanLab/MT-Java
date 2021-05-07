package edu.virginia.psyc.r01.service;


import edu.virginia.psyc.r01.domain.R01Study;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.Scheduled.TextMessage;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class R01TwilioService extends TwilioServiceImpl implements TwilioService {

    public List<ScheduledEvent> messageTypes() {
        List<ScheduledEvent> messages = new ArrayList<>();
        List<String> core_sessions = Arrays.asList(R01Study.SECOND_SESSION, R01Study.THIRD_SESSION,
                R01Study.FOURTH_SESSION, R01Study.FIFTH_SESSION);


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

        messages.add(new TextMessage("gidiHeadsUp",
                "The MindTrails 2 month follow-up is two weeks away! We’ll text you when it’s ready.",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 46, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));
        messages.add(new TextMessage("60day",
                "The MindTrails 2 month follow-up is ready now! Follow this link to complete: " + serverUrl,
                null, R01Study.POST_FOLLOWUP, 60, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("63day",
                "Final MindTrails survey is ready: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 63, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("67day",
                "Time to complete the MindTrails 2 month follow-up! Follow this link to complete: " + serverUrl,
                null, R01Study.POST_FOLLOWUP, 67, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("70day",
                "Final MindTrails survey is ready: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 70, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("75day",
                "Time to complete the MindTrails 2 month follow-up! Follow this link to complete: " + serverUrl,
                null, R01Study.POST_FOLLOWUP, 75, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("81day",
                "Time to complete the MindTrails 2 month follow-up! Follow this link to complete: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.POST_FOLLOWUP, 81, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));



        messages.add(new TextMessage("headsup2",
                "The MindTrails 6 month follow-up is two weeks away! We’ll text you when it’s ready.",
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.FIFTH_SESSION, 106, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("followup_6month",
                "The MindTrails 6 month follow-up is ready now! Follow this link to complete: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.POST_FOLLOWUP2, 120, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("followup2_6month",
                "Time to complete the MindTrails 6 month follow-up! Follow this link to complete it and earn $15: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.POST_FOLLOWUP2, 127, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("followup3_6month",
                "Time to complete the MindTrails 6 month follow-up! Follow this link to complete it and earn $15: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.POST_FOLLOWUP2, 134, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, true));

        messages.add(new TextMessage("followup3_6month",
                "Time to complete the MindTrails 6 month follow-up! Follow this link to complete it and earn $15: " + serverUrl,
                R01Study.STUDY_EXTENSIONS.GIDI.name(),
                R01Study.POST_FOLLOWUP2, 141, ScheduledEvent.SCHEDULE_TYPE.INACTIVITY, false));

        return messages;
    }
}
