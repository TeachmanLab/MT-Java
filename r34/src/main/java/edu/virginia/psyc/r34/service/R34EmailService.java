package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.persistence.Questionnaire.OA;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

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

    @Override
    public List<Email> emailTypes() {
        List<Email> emails = super.emailTypes();
        emails.add(new Email(RISING_SCORE, "MindTrails Alert! Participant score is rising"));
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
    public void sendExample(Email email) {
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
        return;
    }

}
