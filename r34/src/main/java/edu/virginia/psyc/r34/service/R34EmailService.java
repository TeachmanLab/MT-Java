package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.Questionnaire.OA;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.domain.Session;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.List;


/**
 * Adds an additional email to send out to administrators if a participants
 * score is rising.  Seems a bit complicated at first, but the added methods
 * lie emailTypes and sendExample provide for a robust admin interface that
 * allows administrators to see exactly what the messages look like that are
 * being sent out to end users.
 */
@Service
public class R34EmailService extends EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(R34EmailService.class);

    private String RISING_SCORE = "risingScore";


    private String SESSION1 = "SESSION1";
    private String SESSION2 = "SESSION2";
    private String SESSION3 = "SESSION3";
    private String SESSION4 = "SESSION$";
    private String SESSION5 = "SESSION5";
    private String SESSION6 = "SESSION6";


    @Override
    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> emails = super.emailTypes();
        emails.add(new Email(RISING_SCORE, "MindTrails Alert! Participant score is rising"));
        emails.add(new Email(SESSION1, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SESSION2, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SESSION3, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SESSION4, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SESSION5, "Bonus feature from the MindTrails team"));
        emails.add(new Email(SESSION6, "Bonus feature from the MindTrails team"));
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
    public void sendExample(Email email) throws MessagingException {
        if(email.getType().equals(RISING_SCORE)) {
            OA firstEntry, secondEntry;
            firstEntry = new OA(0,0,1,0,2);
            secondEntry = new OA(5,4,0,3,1);
            sendAtRiskAdminEmail(email.getParticipant(), firstEntry, secondEntry);
        } else {
            super.sendExample(email);
        }
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        Session currentSession = participant.getStudy().getCurrentSession();
        Email email = null;
        if (currentSession.getName().equals(R34Study.NAME.SESSION1.toString())) {
            email = new Email("SESSION1","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R34Study.NAME.SESSION2.toString())) {
            email = new Email("SESSION2","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R34Study.NAME.SESSION3.toString())) {
            email = new Email("SESSION3","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R34Study.NAME.SESSION4.toString())) {
            email = new Email("SESSION4","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R34Study.NAME.SESSION5.toString())) {
            email = new Email("SESSION5","Bonus feature from the MindTrails team");
        } else if (currentSession.getName().equals(R34Study.NAME.SESSION6.toString())) {
            email = new Email("SESSION6","Bonus feature from the MindTrails team");
        }
        if (email != null) {
            email.setTo(participant.getEmail());
            email.setParticipant(participant);
            email.setContext(new Context());
            sendEmail(email);
        }
    }

}
