package edu.virginia.psyc.pi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/21/14
 * Time: 2:36 PM
 * A Scheduled task that runs once daily at 2 am.  Based on a set of simple rules
 * it determines when it should send a reminder email.
 */
@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void test() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
        try {
            sendSimpleMail("dan", "daniel.h.funk@gmail.com");
        } catch (MessagingException me) {
            System.out.println("Failed to send message:" + me.getMessage());
        }
    }

    @Scheduled(cron="0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() {
        // Do something intelligent here.
    }


    /*
  * Send HTML mail (simple)
  */
    public void sendSimpleMail(
            final String recipientName, final String recipientEmail)
            throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Example HTML email (simple)");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email/email-simple", ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.mailSender.send(mimeMessage);
    }



}
