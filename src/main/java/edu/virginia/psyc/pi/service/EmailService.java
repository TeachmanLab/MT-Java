package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Study;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.EmailLogDAO;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.OA;
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
import java.util.List;

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
    public enum TYPE {
        day2, day4, day7, day11, day15, day18,
        followup, followup2, followup3,
        resetPass, alertAdmin, giftCard, exportError
    }

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

    @Value("${email.alertsTo}")
    private String alertsTo;

    @Value("${email.admin}")
    private String adminTo;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private String getSubject(TYPE type) {

        switch (type) {
            case day2:
                return "Update from the Project Implicit Mental Health training team";
            case day4:
                return "Update from the Project Implicit Mental Health training team";
            case day7:
                return "Important reminder from the Project Implicit Mental Health training team";
            case day11:
                return "Continuation in the Project Implicit Mental Health training study";
            case day15:
                return "Final reminder re. continuation in the Project Implicit Mental Health training study";
            case day18:
                return "Closure of account in Project Implicit Mental Health training study";
            case followup:
                return "Follow-up from the Project Implicit Mental Health training team";
            case followup2:
                return "Follow-up reminder from the Project Implicit Mental Health training team";
            case followup3:
                return "Final reminder from the Project Implicit Mental Health training team";
            case resetPass:
                return "Project Implicit Mental Health - Account Request";
            case alertAdmin:
                return "PIMH Alert! a participants score is Dropping";
            case giftCard:
                return "Project Implicit Mental Health - Your gift card!";
            case exportError:
                return "MindTrails - Export Failure!";
            default:
                return "";
        }
    }

  /*
  * Send HTML mail using a template named according to Type.
  */
    private void sendMail(String address, Long id, TYPE type, Context ctx) throws MessagingException{
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(getSubject(type));
        message.setFrom(this.respondTo);
        message.setTo(address);
        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("email/" + type.toString(), ctx);
        message.setText(htmlContent, true /* isHtml */);
        // Send email
        this.mailSender.send(mimeMessage);
        // Log that the email was sent.
        logEmail(id, type);
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

        sendMail(participant.getEmail(), participant.getId(), type, ctx);
    }

    public void sendPasswordReset(Participant participant) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();
        ctx.setVariable("token", participant.getPasswordToken().getToken());

        sendMail(participant, TYPE.resetPass, ctx);
    }

    public void sendAtRiskAdminEmail(Participant participant, OA firstEntry, OA currentEntry) throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context();

        ctx.setVariable("name", "PIMH-CBM Administrator");
        ctx.setVariable("url", this.siteUrl);
        ctx.setVariable("respondTo", this.respondTo);
        ctx.setVariable("participant", participant);
        ctx.setVariable("orig", firstEntry);
        ctx.setVariable("latest", currentEntry);

        sendMail(this.alertsTo, participant.getId(), TYPE.alertAdmin, ctx);
    }
    /**
     * Sends an alert message to an administrative account, letting them know about a problem with the system.
     * @param alertMessage
     */
    public void sendExportAlertEmail(String alertMessage) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();

        ctx.setVariable("name", "PIMH-CBM Administrator");
        ctx.setVariable("url", this.siteUrl);
        ctx.setVariable("respondTo", this.respondTo);
            ctx.setVariable("message", alertMessage);
        sendMail(this.adminTo, 0l, TYPE.exportError, ctx);
    }

    public void sendGiftCardEmail(Participant participant, Reward reward, int amount) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context();

        ctx.setVariable("reward", reward);
        ctx.setVariable("giftAmount", amount);
        sendMail(participant, TYPE.giftCard, ctx);
    }

    public void sendSimpleMail(Participant participant, TYPE type) throws MessagingException {
        // Prepare the evaluation context
        LOG.info("SENDING MAIL: " + participant.getEmail() + "\t" + type + "\t" + participant.isEmailOptout());
        final Context ctx = new Context();
        sendMail(participant, type, ctx);
    }


    /**
     * Records the sending of an email.
     *
     * @param id
     * @param type
     */
    private void logEmail(long id, TYPE type) {
        ParticipantDAO participantDAO;
        EmailLogDAO logDAO;

        LOG.info("Sent an email to participant #" + id + " of type " + type);
        participantDAO = participantRepository.findOne(id);
        logDAO = new EmailLogDAO(participantDAO, type);
        if(participantDAO != null) {  // Don't die if you can't log the email.
            participantDAO.addEmailLog(logDAO);
            participantRepository.save(participantDAO);
        }
    }

    @Scheduled(cron = "0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() throws MessagingException {
        List<ParticipantDAO> participants;
        Participant participant;

        participants = participantRepository.findAll();
        TYPE type;

        for(ParticipantDAO dao : participants) {
            participant = participantRepository.entityToDomain(dao);
            type = getEmailToSend(participant);
            if(type != null) sendSimpleMail(participant, type);
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
    public TYPE getEmailToSend(Participant p) {
        TYPE type = null;

        // Never send more than one email a day.
        if (p.daysSinceLastEmail() < 2) return null;

        // Never send email to an inactive participant;
        if (!p.isActive()) return null;

        int days = p.daysSinceLastMilestone();

        Study s = p.getStudy();

        // Remind people between sessions until they complete session 8.
        if (!s.completed(CBMStudy.NAME.SESSION8.toString())) {
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
            }
        }

        // Follow up emails should only be send if session 8 was completed, and
        // the POST Session was not yet completed.
        if (s.completed(CBMStudy.NAME.SESSION8.toString()) && !s.completed(CBMStudy.NAME.POST.toString())) {
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
            }
        }

        return type;
    }


}
