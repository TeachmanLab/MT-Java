package org.mindtrails.service;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

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

    @Value("${email.imageServerUrl}")
    protected String siteUrl;

    @Value("${email.respondTo}")
    protected String respondTo;

    @Value("${email.alertsTo}")
    protected String alertsTo;

    @Value("${email.admin}")
    protected String adminTo;


    protected String getSubject(String type) {
        return getSubject(TYPE.valueOf(type));
    }

    protected String getSubject(TYPE type) {
        switch (type) {
            case resetPass:
                return "MindTrails - Account Request";
            case alertAdmin:
                return "MindTrails Alert! a participants score is raising";
            case giftCard:
                return "MindTrails - Your gift card!";
            case exportError:
                return "MindTrails - Export Failure!";
            default:
                return "";
        }
    }

    @Override
    public void sendEmail(Participant participant, String type) throws MessagingException {
        // Prepare the evaluation context
        LOG.info("SENDING MAIL: " + participant.getEmail() + "\t" + type + "\t" + participant.isEmailOptout());
        final Context ctx = new Context();
        sendMail(participant, type, ctx);
    }

    /*
     * Does a lookup of the subject based on type.
     */
    private void sendMail(Participant participant, String type,  Context ctx)
            throws MessagingException {

        // Prepare the evaluation context
        ctx.setVariable("name", participant.getFullName());
        ctx.setVariable("giftCards", participant.isReceiveGiftCards());
        ctx.setVariable("url", this.siteUrl);
        ctx.setVariable("respondTo", this.respondTo);

        sendMail(participant.getEmail(), type, ctx);
        logEmail(participant, type);
    }

    protected void sendMail(String address,  String type, Context ctx) throws MessagingException {
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(getSubject(type));
        message.setFrom(this.respondTo);
        message.setTo(address);
        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email/" + type, ctx);
        message.setText(htmlContent, true /* isHtml */);
        // Send email
        this.mailSender.send(mimeMessage);
        LOG.info("Sent an email of type " + type);
    }


    /**
     * Records the sending of an email.
     *
     * @param participant
     * @param type
     */
    private void logEmail(Participant participant, String type) {
        EmailLog log;
        log = new EmailLog(participant, type);
        if(participant != null) {  // Don't die if you can't log the email.
            participant.addEmailLog(log);
            participantRepository.save(participant);
        }
    }

    /**
     * Sends an alert message to an administrative account, letting them know about a problem with the system.
     * @param alertMessage
     */
    @Override
    public void sendExportAlertEmail(String alertMessage) {
        // Prepare the evaluation context
        final Context ctx = new Context();

        ctx.setVariable("name", "PIMH-CBM Administrator");
        ctx.setVariable("url", this.siteUrl);
        ctx.setVariable("respondTo", this.respondTo);
        ctx.setVariable("message", alertMessage);
        try {
            sendMail(this.adminTo, TYPE.exportError.toString(), ctx);
        } catch (MessagingException e) {
            LOG.error("Encountered a error sending an alert message" + e.getMessage());
        }
    }

    @Override
    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("token", participant.getPasswordToken().getToken());
        sendMail(participant, TYPE.resetPass.toString(), ctx);
    }

    @Override
    public void sendGiftCard(Participant participant, Reward reward, int amount) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("reward", reward);
        ctx.setVariable("giftAmount", amount);
        sendMail(participant, TYPE.giftCard.toString(), ctx);
    }

    @Override
    public Map<String,String> emailTypes() {
        Map<String,String> types = new HashMap<>();
        for(TYPE t : TYPE.values()) {
            types.put(t.toString(), getSubject(t));
        }
        return types;
    }

}
