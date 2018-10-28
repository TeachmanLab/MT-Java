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

    private String FIRST_SESSION = "SESSION1";
    private String SECOND_SESSION = "SESSION2";
    private String THIRD_SESSION = "SESSION3";
    private String FOURTH_SESSION = "SESSION4";


    @Override
    public List<Email> emailTypes() {
        List<Email> emails = super.emailTypes();
        emails.add(new Email(FIRST_SESSION, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SECOND_SESSION, "Bonus feature from the MindTrails team"));
        emails.add(new Email(THIRD_SESSION, "Bonus feature from the MindTrails team"));
        emails.add(new Email(FOURTH_SESSION, "Bonus feature from the MindTrails team"));
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
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;



        if (currentSession.getName().equals(R01Study.FIRST_SESSION.toString())) {
            email = new Email("SESSION1","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R01Study.SECOND_SESSION.toString())) {
            email = new Email("SESSION2","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R01Study.THIRD_SESSION.toString())) {
            email = new Email("SESSION3","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R01Study.FOURTH_SESSION.toString())) {
            email = new Email("SESSION4","Bonus feature from the MindTrails team");
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
