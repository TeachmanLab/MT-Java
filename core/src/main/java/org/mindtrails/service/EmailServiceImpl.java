package org.mindtrails.service;

import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides all the basic elements needed for an email service.
 * This class should be extended and receive the @service annotation
 * so it is available to the rest of the application as needed.
 */
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    protected JavaMailSender mailSender;

    @Autowired
    protected TemplateEngine templateEngine;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Value("${server.url}")
    protected String siteUrl;

    @Value("${email.respondTo}")
    protected String respondTo;

    @Value("${email.alertsTo}")
    protected String alertsTo;

    @Value("${email.admin}")
    protected String adminTo;

    @Override
    public List<Email> emailTypes() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email(TYPE.resetPass.toString(), "MindTrails - Account Request"));
        emails.add(new Email(TYPE.alertAdmin.toString(), "MindTrails Alert!"));
        emails.add(new Email(TYPE.giftCard.toString(), "MindTrails - Your gift card!"));
        emails.add(new Email(TYPE.day2.toString(), "Update from the MindTrails project team"));
        emails.add(new Email(TYPE.day4.toString(), "Update from the MindTrails project team"));
        emails.add(new Email(TYPE.day7.toString(), "Important reminder from the MindTrails project team"));
        emails.add(new Email(TYPE.day11.toString(), "Continuation in the MindTrails project study"));
        emails.add(new Email(TYPE.day15.toString(), "Final reminder re. continuation in the MindTrails project study"));
        emails.add(new Email(TYPE.day18.toString(), "Closure of account in MindTrails project study"));
        emails.add(new Email(TYPE.followup.toString(), "Follow-up from the MindTrails project team"));
        emails.add(new Email(TYPE.followup2.toString(), "Follow-up reminder from the MindTrails project team"));
        emails.add(new Email(TYPE.followup3.toString(), "Final reminder from the MindTrails project team"));
        emails.add(new Email(TYPE.debrief.toString(), "Explanation of the MindTrails project"));
        return emails;
    }

    public Email getEmailForType(String type) {
        for(Email e : emailTypes()) {
            if(e.getType().equals(type)) return e;
        }
        throw new RuntimeException("Unknown Email type:" + type);
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        return;
    }

    public void sendEmail(Email email) {
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        email.getContext().setVariable("url", this.siteUrl);
        email.getContext().setVariable("respondTo", this.respondTo);
        email.getContext().setVariable("participant", email.getParticipant());

        try {
            message.setSubject(email.getSubject());
            message.setFrom(this.respondTo);
            message.setTo(email.getTo());
            // Create the HTML body using Thymeleaf
            final String htmlContent = this.templateEngine.process("email/" + email.getType(), email.getContext());
            message.setText(htmlContent, true /* isHtml */);
            // Send email
            this.mailSender.send(mimeMessage);
            logEmail(email, null);
        } catch (MessagingException me) {
            logEmail(email, me);
        }
    }

    /**
     * defaults to the standard sendEamil, but can be overridden if you need to send
     * out a custom email with additional parameters.
     * @param email
     */
    public void sendExample(Email email) {
        sendEmail(email);
    }

    /**
     * Records the sending of an email.
     *
     * @param email
     */
    private void logEmail(Email email, Exception e) {
        EmailLog log;
        Participant participant = email.getParticipant();

        if(e == null) LOG.info("Sent an email of type " + email.getType());
        if(e != null) LOG.error("Failed to send an email of type " +
                                email.getType()  + "; " + e.getLocalizedMessage());

        if(participant != null) {  // This associates emails to participants, no need if not there.
            log = new EmailLog(email);
            if(e != null) log.setError(e);
            participant.addEmailLog(log);
            participantRepository.save(participant);
        }
    }



    /**
     * Sends an alert message to an administrative account, letting them know
     * about a problem with the system.
     * @param alertMessage
     */
    @Override
    public void sendAdminEmail(String alertSubject, String alertMessage) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType(TYPE.alertAdmin.toString());
        email.setSubject(email.getSubject() + " " + alertSubject);
        email.setTo(this.adminTo);
        email.setContext(ctx);
        ctx.setVariable("message", alertMessage);
        sendEmail(email);
    }

    @Override
    public void sendPasswordReset(Participant participant){
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType(TYPE.resetPass.toString());
        email.setContext(ctx);
        email.setTo(participant.getEmail());
        email.setParticipant(participant);

        ctx.setVariable("token", participant.getPasswordToken().getToken());
        sendEmail(email);
    }

    @Override
    public void sendGiftCard(Participant participant, Reward reward, int amount) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType(TYPE.giftCard.toString());
        email.setTo(participant.getEmail());
        email.setParticipant(participant);
        email.setContext(ctx);

        ctx.setVariable("reward", reward);
        ctx.setVariable("giftAmount", amount);
        sendEmail(email);
    }


    @Scheduled(cron = "0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() {
        List<Participant> participants;

        participants = participantRepository.findAll();
        TYPE type;

        for(Participant participant : participants) {
            type = getTypeToSend(participant);
            if(type != null) {
                Email email = getEmailForType(type.toString());
                email.setTo(participant.getEmail());
                email.setParticipant(participant);
                email.setContext(new Context());
                sendEmail(email);
            }
        }
    }

    /**
     * Given a participant, determines which email to send that
     * participant.  In no email should be sent at this time, then
     * it returns null.  In the future we should consider abstracting
     * out individual emails into classes and have those classes
     * determine if an email should be sent or not.  But the logic
     * here is still pretty simple, so I'm consolidating that code here.
     *
     * @param p
     * @return
     */
    public TYPE getTypeToSend(Participant p) {
        TYPE type = null;

        // Never send more than one email a day.
        if (p.daysSinceLastEmail() < 2) return null;

        // Never send email to an inactive participant;
        if (!p.isActive()) return null;

        int days = p.daysSinceLastMilestone();

        Study study = p.getStudy();
        Session session = study.getCurrentSession();



        // If they are waiting for 2 days, then remind them
        // at the end of 2 days, then again after 4,7,11,15, and 18
        // days since their last session.
        if(session.getDaysToWait() <= 2) {
            switch (days) {
                case 1: // noop;
                    break;
                case 2:
                    type = TYPE.day2;
                    break;
                case 4:
                    type = TYPE.day4;
                    break;
                case 7:
                    type = TYPE.day7;
                    break;
                case 11:
                    type = TYPE.day11;
                    break;
                case 15:
                    type = TYPE.day15;
                    break;
                case 18:
                    type = TYPE.day18;
                    break;
                case 60:
                    type = TYPE.debrief;
                    break;
            }
        }

        // Follow up emails are sent out for tasks for delays of
        // 60 days or more.
        if(session.getDaysToWait() >= 60) {
            switch (days) {
                case 60:
                    type = TYPE.followup;
                    break;
                case 63:
                    type = TYPE.followup2;
                    break;
                case 67:
                    type = TYPE.followup2;
                    break;
                case 70:
                    type = TYPE.followup2;
                    break;
                case 75:
                    type = TYPE.followup3;
                    break;
                case 120:
                    type = TYPE.debrief;
                    break;
            }
        }
        return type;
    }
}
