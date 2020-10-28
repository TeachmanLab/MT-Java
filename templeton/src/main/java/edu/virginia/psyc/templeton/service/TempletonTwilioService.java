package edu.virginia.psyc.templeton.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.service.TwilioServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TempletonTwilioService extends TwilioServiceImpl {



    public String getMessage(Participant p) {
        String message = null;

        int daysSinceLastMilestone = p.daysSinceLastMilestone();
        Study study = p.getStudy();
        Session session = study.getCurrentSession();

        if (session.getDaysToWait() <= 2) {
            switch (daysSinceLastMilestone) {
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
        if (session.getDaysToWait() >= 60) {
            switch (daysSinceLastMilestone) {
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

    @Override
    public List<ScheduledEvent> messageTypes() {
        return new ArrayList<>();
    }
}
