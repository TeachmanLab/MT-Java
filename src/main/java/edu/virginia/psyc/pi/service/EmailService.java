package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.persistence.EmailLogDAO;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/21/14
 * Time: 2:36 PM
 * Runs as a scheduled service.  Based on a set of simple rules
 * it determines when and to whom it should send a reminder email.
 * This is tightly coupled to html files in resources/templates/email
 */
@Service
public class EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    /**
     * Each of these types should have a coresponding template in resources/templates/email
     */
    public enum TYPE {day2, day4, day7, day11, day15, day18, followup, followup2, followup3, resetPass}

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ParticipantRepository participantRepository;

    @Value("${email.imageServerUrl}")
    private String siteUrl;

    @Value("${email.respondTo}")
    private String respondTo;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private String getSubject(TYPE type) {

        switch (type) {
            case day2      : return "Update from the Project Implicit Mental Health training team";
            case day4      : return "Update from the Project Implicit Mental Health training team";
            case day7      : return "Important reminder from the Project Implicit Mental Health training team";
            case day11     : return "Continuation in the Project Implicit Mental Health training study";
            case day15     : return "Final reminder re. continuation in the Project Implicit Mental Health training study";
            case day18     : return "Closure of account in Project Implicit Mental Health training study";
            case followup  : return "Follow-up from the Project Implicit Mental Health training team";
            case followup2 : return "Follow-up reminder from the Project Implicit Mental Health training team";
            case followup3 : return "Final reminder from the Project Implicit Mental Health training team";
            case resetPass : return "Project Implicit Mental Health - Account Request";
            default        : return "";
        }
    }

    /*
  * Send HTML mail (simple)
  */
    private void sendMail(Participant participant, TYPE type, Context ctx)
            throws MessagingException {

        // Prepare the evaluation context
        ctx.setVariable("name", participant.getFullName());
        ctx.setVariable("url", this.siteUrl);
        ctx.setVariable("respondTo", this.respondTo);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(getSubject(type));
        message.setFrom(this.respondTo);
        message.setTo(participant.getEmail());

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email/" + type.toString(), ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.mailSender.send(mimeMessage);

        // Log that the email was sent.
        logEmail(participant.getId(), type);
    }

    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("token", participant.getPasswordToken().getToken());

        sendMail(participant, TYPE.resetPass, ctx);
    }

    public void sendSimpleMail(Participant participant, TYPE type) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();
        sendMail(participant, type, ctx);

    }


    /**
     * Records the sending of an email.
     * @param id
     * @param type
     */
    private void logEmail(long id, TYPE type) {
        ParticipantDAO participantDAO;
        EmailLogDAO    logDAO;

        LOG.info("Sent an email to participant #" + id + " of type " + type);
        participantDAO = participantRepository.findOne(id);
        logDAO         = new EmailLogDAO(participantDAO, EmailService.TYPE.day2);
        participantDAO.addLog(logDAO);
        participantRepository.save(participantDAO);
        LOG.info("Participant updated.");
    }

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void test() {
        //System.out.println("The time is now " + dateFormat.format(new Date()));
        /*
        try {
            sendSimpleMail("dan", "daniel.h.funk@gmail.com");
        } catch (MessagingException me) {
            System.out.println("Failed to send message:" + me.getMessage());
        }
        */
    }

    @Scheduled(cron="0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() {
        // Do something intelligent here.
    }



}
