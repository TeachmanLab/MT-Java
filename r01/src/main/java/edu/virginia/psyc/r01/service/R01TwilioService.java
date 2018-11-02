package edu.virginia.psyc.r01.service;


import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.service.TwilioService;
import org.mindtrails.service.TwilioServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class R01TwilioService extends TwilioServiceImpl implements TwilioService {

    @Override
    public String getMessage(Participant p) {
        String message = "";
        int daysSinceLastMilestone = p.daysSinceLastMilestone();
        Study study = p.getStudy();
        Session session = study.getCurrentSession();

        if (session.getDaysToWait() <= 10) {
            switch (daysSinceLastMilestone) {
                case 7:
                    message = "Time to start your next MindTrails Session!  Visit :" + serverUrl;
                    break;
                case 10:
                    message = "Complete your next MindTrails session soon!  Visit :" + serverUrl;
                    break;
                case 14:
                    message = "It has been two weeks since your last MindTrails Session.  Start Now :" + serverUrl;
                    break;
                case 18:
                    message = "Final reminder about your MindTrails Account!  You can start back up here:" + serverUrl;
                    break;
                case 21:
                    message = "MindTrails account closed. Would you mind completing a quick survey?"
                            + serverUrl + "'/questions/ReasonsForEnding'";
                    break;
            }
        }

        // Follow up emails are sent out for tasks for delays of
        // 60 days or more.1
        if (session.getDaysToWait()  >= 60) {
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
}
