package org.mindtrails.service;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.FixedUidGenerator;
import net.fortuna.ical4j.util.MapTimeZoneCache;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.mindtrails.domain.*;
import org.mindtrails.domain.Scheduled.Email;
import org.mindtrails.domain.Scheduled.ScheduledEvent;
import org.mindtrails.domain.tango.ExchangeRates;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    protected ParticipantService participantService;

    @Autowired
    protected TaskLogRepository taskLogRepository;

    @Autowired
    protected ResourceLoader resourceLoader;

    @Value("${server.url}")
    protected String siteUrl;

    @Value("${email.respondTo}")
    protected String respondTo;

    @Value("${email.alertsTo}")
    protected String alertsTo;

    @Value("${email.admin}")
    protected String adminTo;

    public List<ScheduledEvent> emailTypes() {
        List<ScheduledEvent> emails = new ArrayList<>();
        // These are EVENT based emails that are called out
        emails.add(new Email("resetPass", "MindTrails - Account Request"));
        emails.add(new Email("alertAdmin", "MindTrails Alert!"));
        emails.add(new Email("giftCard", "Your E-Gift Card!"));
        emails.add(new Email("midSessionStop", "Incomplete Session Notice from the MindTrails Project Team"));
        return  emails;
    }

    public Email getEmailForType(String type) {
        for (ScheduledEvent e : emailTypes()) {
            if (e.getType().equals(type)) return (Email)e;
        }
        throw new RuntimeException("Unknown Email type:" + type);
    }

    public boolean emailTemplateExists(String type) {
        Resource resource = resourceLoader.getResource("classpath:/templates/email/" + type + ".html");
        return resource.exists();
        }

    @Override
    public void sendSessionCompletedEmail(Participant participant) {
        return;
    }


    private String getStudyName() {
        List<Study> studies = participantService.getStudies();
        String name = "";
        for(Study s : studies) { // Typically just one, so use that.
            name = s.getName();
        }
        return name;
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
            email.getContext().setVariable("studyName", getStudyName());
            if (email.getParticipant() != null) {
                email.getContext().setLocale(email.getParticipant().locale());
            }
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
    public void sendGiftCard(Participant participant, OrderResponse order, GiftLog log) {
        // Prepare the evaluation context
        final Context ctx = new Context();
        Email email = getEmailForType("giftCard");
        email.setTo(participant.getEmail());
        email.setParticipant(participant);
        email.setContext(ctx);

        if(log.getCurrency().equals(ExchangeRates.US_DOLLARS)) {
            ctx.setVariable("amount",
                    "$" + log.getAmount());
        } else {
            ctx.setVariable("amount",
                    log.getAmount() + " " + log.getCurrency() +
                            " ($" + (int)log.getDollarAmount() + ")");
        }

        ctx.setVariable("order", order);
        sendEmail(email);
    }


    @ExportMode
    @Scheduled(cron = "0 0 * * * *")   // Runs every hour.
    /**
     * Sends emails to participants when they stopped in the middle of a session
     * and didn't return to it for 3 hours.
     */
    @Transactional(propagation= Propagation.REQUIRED, readOnly=false, noRollbackFor=Exception.class)
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


    Calendar getInvite(Participant participant, Date date) {

        // This corrects for an iCal issue which defaults to having a dependency on a caching service
        // https://github.com/ical4j/ical4j/issues/195q
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", MapTimeZoneCache.class.getName());

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
