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
import java.util.Arrays;
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
        List<String> core_sessions = Arrays.asList(R01Study.SECOND_SESSION, R01Study.THIRD_SESSION,
                R01Study.FOURTH_SESSION, R01Study.FIFTH_SESSION;


        emails.add(new Email("risingScore", "MindTrails Alert! Participant Score Is Rising"));
<<<<<<< HEAD
        emails.add(new Email("day7", "Update from the MindTrails Project Team"));
        emails.add(new Email("day10", "Update from the MindTrails Project Team"));
        emails.add(new Email("day14", "Update from the MindTrails Project Team"));
        emails.add(new Email("day18", "Important Reminder from the MindTrails Project Team"));

        emails.add(new Email("headsup1", "The MindTrails Two Month Follow-Up Survey is Just 2 Weeks Away!"));

        emails.add(new Email("followup", "It’s Time to Complete the MindTrails Two Month Follow-Up Survey!"));
        emails.add(new Email("followup2", " We’d love to hear from you!"));
        emails.add(new Email("followup3", "We’d love to help!"));

        emails.add(new Email("headsup2", "The MindTrails Six Month Follow-Up Survey is Just 2 Weeks Away!"));
        emails.add(new Email("followup1_6month", "It’s Time to Complete the MindTrails Six Month Follow-Up Survey!"));
        emails.add(new Email("followup2_6month", "We’d love to hear from you!"));
        emails.add(new Email("followup3_6month", "We’d love to help!"));
        emails.add(new Email("followup3", "We’d love to help!"));

        emails.add(new Email("SESSION1", "Bonus Feature #1 from the MindTrails Project Team"));
        emails.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team"));
        emails.add(new Email("SESSION3", "Bonus Feature #3 from the MindTrails Project Team"));
        emails.add(new Email("SESSION4", "Bonus Feature #4 from the MindTrails Project Team"));
=======

        // Bonus feature emails that come at the end of a session.
        emails.add(new Email("SESSION1", "Bonus Feature #1 from the MindTrails Project Team",
                null, R01Study.SECOND_SESSION, 0, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("SESSION2", "Bonus Feature #2 from the MindTrails Project Team",
                null, R01Study.THIRD_SESSION, 0, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("SESSION3", "Bonus Feature #3 from the MindTrails Project Team",
                null, R01Study.FOURTH_SESSION, 0, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("SESSION4", "Bonus Feature #4 from the MindTrails Project Team",
                null, R01Study.FIFTH_SESSION, 0, Email.SCHEDULE_TYPE.INACTIVITY));

        // Reminder emails when users are inactive longer than they should be in the core sessions (2,3,4,5)
        emails.add(new Email("day7", "Update from the MindTrails Project Team",
                null, core_sessions, 7, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("day10", "Update from the MindTrails Project Team",
                null, core_sessions, 10, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("day14", "Update from the MindTrails Project Team",
                null, core_sessions, 14, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("day18", "Important Reminder from the MindTrails Project Team",
                null, core_sessions, 18, Email.SCHEDULE_TYPE.INACTIVITY));

        // TET Notifications in Followup Session
        emails.add(new Email("followup", "Follow-up Reminder from the MindTrails Project Team",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, 60, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("followup2", "Follow-up Reminder from the MindTrails Project Team",
                R01Study.STUDY_EXTENSIONS.TET.name(), R01Study.POST_FOLLOWUP, Arrays.asList(63, 67, 70), Email.SCHEDULE_TYPE.INACTIVITY));

        // GIDI Reminders
        emails.add(new Email("gidi_reminder1", "The MindTrails Two Month Follow-Up Survey is Just 2 Weeks Away!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 46, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("gidi_reminder2", "It’s Time to Complete the MindTrails Two Month Follow-Up Survey!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 60, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("gidi_reminder3", "We’d love to hear from you!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 67, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("gidi_reminder4", "We’d love to help!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 74, Email.SCHEDULE_TYPE.INACTIVITY));
        // Gidi Reminder #5 (which is missing here) is missing because it is SMS only.
        emails.add(new Email("gidi_reminder6", "The MindTrails Six Month Follow-Up Survey is Just 2 Weeks Away!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 166, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("gidi_reminder7", "It’s Time to Complete the MindTrails Six Month Follow-Up Survey!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 180, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("gidi_reminder8", "We’d love to hear from you!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 7, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("gidi_reminder9", "We’d love to help!",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP2, 14, Email.SCHEDULE_TYPE.INACTIVITY));

        // GIDI Newsletter Emails
        emails.add(new Email("newsletter1", "Mindtrails Newsletter #1",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.FIRST_SESSION, 0, Email.SCHEDULE_TYPE.INACTIVITY));
        emails.add(new Email("newsletter2", "Mindtrails Newsletter #2",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 7, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("newsletter3", "Mindtrails Newsletter #3",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 30, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("newsletter4", "Mindtrails Newsletter #4",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 90, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("newsletter5", "Mindtrails Newsletter #5",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 120, Email.SCHEDULE_TYPE.SINCE_COMPLETION));
        emails.add(new Email("newsletter6", "Mindtrails Newsletter #6",
                R01Study.STUDY_EXTENSIONS.GIDI.name(), R01Study.POST_FOLLOWUP, 150, Email.SCHEDULE_TYPE.SINCE_COMPLETION));

>>>>>>> 45ecc400150baf1243aa4a6113eab5ef421d955a
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

        for(Email email: emailTypes()) {

            if(email.getSessions().contains(session.getName()) ) {

            }

        }
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;

        if(!participant.isActive() || !participant.isEmailReminders())  return;  // Don't send these messages if they should not get them.

        if (currentSession.getName().equals(R01Study.SECOND_SESSION.toString())) {
            email = new Email("SESSION1","Bonus Feature #1 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.THIRD_SESSION.toString())) {
            email = new Email("SESSION2","Bonus Feature #2 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.FOURTH_SESSION.toString())) {
            email = new Email("SESSION3","Bonus Feature #3 from the MindTrails Project Team");
        } else if (currentSession.getName().equals(R01Study.FIFTH_SESSION.toString())) {
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
