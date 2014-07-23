package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public enum TYPE {day2, day4, day7, day11, day15, day18, followup, followup2, followup3}

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

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
            default        : return "";
        }
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


    /*
  * Send HTML mail (simple)
  */
    public void sendSimpleMail(Participant participant, TYPE type)
            throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context();
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
    }



}
