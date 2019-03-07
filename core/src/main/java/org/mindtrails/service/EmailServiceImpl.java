package org.mindtrails.service;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.mindtrails.domain.*;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Email> emailTypes() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email("resetPass", "MindTrails - Account Request"));
        emails.add(new Email("alertAdmin", "MindTrails Alert!"));
        emails.add(new Email("giftCard", "Your E-Gift Card!"));
        emails.add(new Email("debrief", "Explanation of the Calm Thinking Study"));
        emails.add(new Email("midSessionStop", "Incomplete Session Notice from the MindTrails Project Team"));
        emails.add(new Email("closure", "Closure of Account in the Calm Thinking Study"));
        emails.add(new Email("debrief", "Explanation of the Calm Thinking Study"));
        return  emails;
    }

    public Email getEmailForType(String type) {
        for (Email e : emailTypes()) {
            if (e.getType().equals(type)) return e;
        }
        throw new RuntimeException("Unknown Email type:" + type);
    }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        return;
    }

    public void sendEmail(Email email) {
        try {

            // Prepare message using a Spring helper
            final MimeMessage message = this.mailSender.createMimeMessage();

            // Create a Multipart
            Multipart multipart = new MimeMultipart();

            message.setSubject(email.getSubject());
            message.setFrom(new InternetAddress(this.respondTo));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));

            // Create the HTML body using Thymeleaf
            email.getContext().setVariable("url", this.siteUrl);
            email.getContext().setVariable("respondTo", this.respondTo);
            email.getContext().setVariable("participant", email.getParticipant());
            final String htmlContent = this.templateEngine.process("email/" + email.getType(),
                    email.getContext());
            MimeBodyPart htmlBodyPart = new MimeBodyPart(); //4
            htmlBodyPart.setContent(htmlContent, "text/html"); //5
            multipart.addBodyPart(htmlBodyPart); // 6

            // Add the calendar invite, if the email has a date.
            if (email.getCalendarDate() != null) {
                // Another part for the calendar invite
                MimeBodyPart invite = new MimeBodyPart();
                invite.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
                invite.setHeader("Content-ID", "calendar_message");
                invite.setHeader("Content-Disposition", "inline");
                invite.setContent(getInvite(email.getParticipant(), email.getCalendarDate()).toString(), "text/calendar");
                multipart.addBodyPart(invite);
            }

            message.setContent(multipart);

            // Send email
            this.mailSender.send(message);
            logEmail(email, null);
        } catch (Exception e) {
            logEmail(email, e);
        }
    }

    /**
     * defaults to the standard sendEamil, but can be overridden if you need to send
     * out a custom email with additional parameters.
     *
     * @param email
     */
    public void sendExample(Email email) throws MessagingException {
        sendEmail(email);
    }

    /**
     * Records the sending of an email.
     *
     * @param email
     */
    private void logEmail(Email email, Exception e) {
        if(email.getParticipant() == null) return; // Don't log emails that are not to a participant.

        EmailLog log;
        Participant participant = participantRepository.findOne(email.getParticipant().getId());

        if (e == null) LOG.info("Sent an email of type " + email.getType());
        if (e != null) LOG.error("Failed to send an email of type " +
                email.getType() + "; " + e.getLocalizedMessage());

        if (participant != null) {  // This associates emails to participants, no need if not there.
            log = new EmailLog(email);
            if (e != null) log.setError(e);
            participant.addEmailLog(log);
            participantRepository.save(participant);
        }
    }


    /**
     * Sends an alert message to an administrative account, letting them know
     * about a problem with the system.
     *
     * @param alertMessage
     */
    @Override
    public void sendAdminEmail(String alertSubject, String alertMessage) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType("alertAdmin");
        email.setSubject(email.getSubject() + " " + alertSubject);
        email.setTo(this.adminTo);
        email.setContext(ctx);
        ctx.setVariable("message", alertMessage);
        sendEmail(email);
    }

    @Override
    public void sendPasswordReset(Participant participant) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType("resetPass");
        email.setContext(ctx);
        email.setTo(participant.getEmail());
        email.setParticipant(participant);

        ctx.setVariable("token", participant.getPasswordToken().getToken());
        sendEmail(email);
    }

    @Override
    public void sendGiftCard(Participant participant, OrderResponse order, int amount) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType("giftCard");
        email.setTo(participant.getEmail());
        email.setParticipant(participant);
        email.setContext(ctx);

        ctx.setVariable("order", order);
        ctx.setVariable("giftAmount", amount);
        sendEmail(email);
    }

    @ExportMode
    @Scheduled(cron = "0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() {
        List<Participant> participants;

        participants = participantRepository.findAll();

        for (Participant participant : participants) {
            String type = getTypeToSend(participant);
            if (type != null) {
                Email email = getEmailForType(type);
                email.setTo(participant.getEmail());
                email.setParticipant(participant);
                email.setContext(new Context());
                sendEmail(email);
            }
        }
    }



    @ExportMode
    @Scheduled(cron = "0 0 * * * *")   // Runs every hour.
    /**
     * Sends emails to participants when they stopped in the middle of a session
     * and didn't return to it for 3 hours.
     */
    public void checkForMidSessionIncompleteAndSendEmails() {
        List<Participant> participants;
        participants = participantRepository.findAll();

        for(Participant participant : participants) {
            if(shouldSendMidSessionReminder(participant)) {
                sendMidSessionEmail(participant);
            }
        }
    }

    public void sendMidSessionEmail(Participant participant) {
        Email email = getEmailForType("midSessionStop");
        email.setTo(participant.getEmail());
        email.setParticipant(participant);
        email.setContext(new Context());
        sendEmail(email);
    }

    public boolean shouldSendMidSessionReminder(Participant participant) {
        if (!participant.isActive()) return false;
        if (!participant.isEmailReminders()) return false;
        Study study = participant.getStudy();
        Session session = study.getCurrentSession();
        if (participant.previouslySent("midSessionStop",
                                        session.getName())) return false;
        if (session.midSession()) {
            LocalDateTime lastTaskDate = LocalDateTime.ofInstant(study.getLastTaskDate().toInstant(), ZoneId.systemDefault());
            if (LocalDateTime.now().minusHours(3).isAfter(lastTaskDate))
                return true;
        }
        return false;
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
    public String getTypeToSend(Participant p) {
        // Never send more than one email a day.
        if (p.daysSinceLastEmail() < 2) return null;

        // Never send email to an inactive participant;
        if (!p.isActive()) return null;


        Study study = p.getStudy();
        Session session = study.getCurrentSession();

        // Don't send emails if they are all done.
        if (study.getState().equals(Study.STUDY_STATE.ALL_DONE)) return null;

        int days = p.daysSinceLastMilestone();

        // Mark the user as inactive if they are about to get a closure email.
        if (getTypeToSend(session, days).equals("closure")) {
            p.setActive(false);
            participantRepository.save(p);
        }

        // Don't send emails to those that requested no reminders.
        if (!p.isEmailReminders()) return null;

        return getTypeToSend(session, days);

    }

    public String getTypeToSend(Session session, int daysSinceLastMilestone) {
        String type = null;

        // If they are waiting for 2 days, then remind them
        // at the end of 2 days, then again after 4,7,11,15, and 18
        // days since their last session.
        if(session.getDaysToWait() <= 2) {
            switch (daysSinceLastMilestone) {
                case 1: // noop;
                    break;
                case 2:
                    type = "day2";
                    break;
                case 4:
                    type = "day4";
                    break;
                case 7:
                    type = "day7";
                    break;
                case 11:
                    type = "day11";
                    break;
                case 15:
                    type = "day15";
                    break;
                case 18:
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


    Calendar getInvite(Participant participant, Date date) {

        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone(participant.getTimezone());
        VTimeZone tz = timezone.getVTimeZone();

        // Use LocalDateTime to create a UTC date for the start time and end time.
        LocalDateTime startDate = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
        LocalDateTime endDate = startDate.plusMinutes(30);

        // Create the event
        String eventName = "Next Mindtrails Session";
        net.fortuna.ical4j.model.DateTime start = new net.fortuna.ical4j.model.DateTime(true);
        net.fortuna.ical4j.model.DateTime end = new net.fortuna.ical4j.model.DateTime(true);
        start.setTime(startDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        end.setTime(endDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        VEvent meeting = new VEvent(start, end, eventName);


        // add timezone info..
        meeting.getProperties().add(tz.getTimeZoneId());

        // generate unique identifier..
        Uid uid;
        try {
            UidGenerator ug = new FixedUidGenerator("uidGen");
            uid = ug.generateUid();
        } catch (SocketException e) {
            UidGenerator ug = new RandomUidGenerator();
            uid = ug.generateUid();
        }
        meeting.getProperties().add(uid);

        // Set a location and description
        meeting.getProperties().add(new Location(this.siteUrl));
        meeting.getProperties().add(new Description("Return to " + this.siteUrl + " to complete your next session."));

        // Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        icsCalendar.getProperties().add(CalScale.GREGORIAN);

        // Add the event and print
        icsCalendar.getComponents().add(meeting);
        System.out.println(icsCalendar);
        return icsCalendar;
    }
}
