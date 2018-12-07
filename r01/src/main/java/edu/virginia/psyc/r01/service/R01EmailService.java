package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.domain.R01Study;
import edu.virginia.psyc.r01.persistence.OA;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A basic email service that behcaves exactly like the core configuration,
 * sending email reminders each day at 2 am and providing endpoints for
 * dealing with standard email messages such as resetting passwords.
 */
@Service
public class R01EmailService extends EmailServiceImpl implements EmailService{

    private String RISING_SCORE = "risingScore";

    @Override
    public List<Email> emailTypes() {
        List<Email> emails = super.emailTypes();
        emails.add(new Email("risingScore", "MindTrails Alert! Participant Score Is Rising"));
        emails.add(new Email("day10", "Update from The MindTrails Project Team"));
        emails.add(new Email("day14", "Update from The MindTrails Project Team"));
        emails.add(new Email("day18", "Important Reminder from The MindTrails Project Team"));
        emails.add(new Email("day21", "Continuation in The MindTrails Project Study"));
        emails.add(new Email("followup", "Follow-up from The MindTrails Project Team"));
        emails.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team"));
        emails.add(new Email("followup3", "Final Reminder from the MindTrails Project Team"));
        emails.add(new Email("SESSION1", "Bonus Feature #1 from the MindTrails Project Team"));
        emails.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team"));
        emails.add(new Email("SESSION3", "Bonus Feature #3 from the MindTrails Project Team"));
        emails.add(new Email("SESSION4", "Bonus Feature #4 from the MindTrails Project Team"));
        return emails;
    }


    public void sendAtRiskAdminEmail(Participant participant, OA firstEntry, OA currentEntry)  {

        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("orig", firstEntry);
        ctx.setVariable("latest", currentEntry);

        Email email = getEmailForType(RISING_SCORE);
        email.setContext(ctx);
        email.setTo(this.alertsTo);
        email.setParticipant(participant);

        sendEmail(email);
    }


    @Override
    public String getTypeToSend(Session session, int daysSinceLastMilestone) {
        String type = null;

        // If they are waiting for 2 days, then remind them
        // at the end of 2 days, then again after 4,7,11,15, and 18
        // days since their last session.
        if(session.getDaysToWait() <= 7) {
            switch (daysSinceLastMilestone) {
                case 7:
                    type = "day7";
                    break;
                case 10:
                    type = "day10";
                    break;
                case 14:
                    type = "day14";
                    break;
                case 18:
                    type = "day18";
                    break;
                case 21:
                    type = "closure";
                    break;
            }
        }

        // Follow up emails are sent out for tasks for delays of
        // 60 days or more.
        if(session.getDaysToWait() >= 60) {
            switch (daysSinceLastMilestone) {
                case 60:
                    type = "followup";
                    break;
                case 63:
                    type = "followup2";
                    break;
                case 67:
                    type = "followup2";
                    break;
                case 70:
                    type = "followup2";
                    break;
                case 75:
                    type = "followup3";
                    break;
                case 120:
                    type = "debrief";
                    break;
            }
        }
        return type;

    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;

        if(!participant.isEmailReminders())  return;  // Don't send these messages if they should not get them.

        if (currentSession.getName().equals(R01Study.FIRST_SESSION.toString())) {
            email = new Email("SESSION1","Bonus Feature #1 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.SECOND_SESSION.toString())) {
            email = new Email("SESSION2","Bonus Feature #2 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.THIRD_SESSION.toString())) {
            email = new Email("SESSION3","Bonus Feature #3 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.FOURTH_SESSION.toString())) {
            email = new Email("SESSION4","Bonus Feature #4 from the MindTrails Project Team");
        }
        if (email != null) {

            // If the date the participant is returning is after today, then include a calendar invite
            // in this follow up email.
            if(participant.getReturnDate() != null && participant.getReturnDate().after(new Date()))
                email.setCalendarDate(participant.getReturnDate());

            email.setTo(participant.getEmail());
            email.setParticipant(participant);
            email.setContext(new Context());
            sendEmail(email);
        }
    }


}
