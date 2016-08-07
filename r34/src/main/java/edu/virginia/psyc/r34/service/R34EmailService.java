package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.Questionnaire.OA;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.service.EmailService;
import org.mindtrails.service.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;


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
public class R34EmailService extends EmailServiceImpl implements EmailService {
    private static final Logger LOG = LoggerFactory.getLogger(R34EmailService.class);

    /**
     * Each of these types should have a coresponding template in resources/templates/email
     */
    public enum R34_EMAIL {
        day2, day4, day7, day11, day15, day18,
        followup, followup2, followup3,
        resetPass, alertAdmin, giftCard, exportError
    }

    @Override
    protected String getSubject(String emailType) {
        String subject = super.getSubject(emailType);
        if(subject.isEmpty()) subject = getSubject(R34_EMAIL.valueOf(emailType));
        return subject;
    }

    private String getSubject(R34_EMAIL type) {
        switch (type) {
            case day2:
                return "Update from the MindTrails project team";
            case day4:
                return "Update from the MindTrails project team";
            case day7:
                return "Important reminder from the MindTrails project team";
            case day11:
                return "Continuation in the MindTrails project study";
            case day15:
                return "Final reminder re. continuation in the MindTrails project study";
            case day18:
                return "Closure of account in MindTrails project study";
            case followup:
                return "Follow-up from the MindTrails project team";
            case followup2:
                return "Follow-up reminder from the MindTrails project team";
            case followup3:
                return "Final reminder from the MindTrails project team";
            case alertAdmin:
                return "MindTrails Alert! a participants score is raising";
            default:
                return "";
        }
    }

    @Override
    public Map<String,String> emailTypes() {
        Map<String,String> types = super.emailTypes();
        for(R34_EMAIL t : R34_EMAIL.values()) {
            types.put(t.toString(), getSubject(t));
        }
        return types;

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

        sendMail(this.alertsTo, R34_EMAIL.alertAdmin.toString(), ctx);
    }

    @Scheduled(cron = "0 0 2 * * *")  // schedules task for 2:00am every day.
    public void sendEmailReminder() throws MessagingException {
        List<Participant> participants;

        participants = participantRepository.findAll();
        R34_EMAIL type;

        for(Participant participant : participants) {
            type = getEmailToSend(participant);
            if(type != null) sendEmail(participant, type.toString());
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
    public R34_EMAIL getEmailToSend(Participant p) {
        R34_EMAIL type = null;

        // Never send more than one email a day.
        if (p.daysSinceLastEmail() < 2) return null;

        // Never send email to an inactive participant;
        if (!p.isActive()) return null;

        int days = p.daysSinceLastMilestone();

        Study s = p.getStudy();

        // Remind people between sessions until they complete session 8.
        if (!s.completed(R34Study.NAME.SESSION8.toString())) {
           switch (days) {
                case 1: // noop;
                    break;
                case 2:
                    type = R34_EMAIL.day2;
                    break;
                case 4:
                    type = R34_EMAIL.day4;
                    break;
                case 7:
                    type = R34_EMAIL.day7;
                    break;
                case 11:
                    type = R34_EMAIL.day11;
                    break;
                case 15:
                    type = R34_EMAIL.day15;
                    break;
                case 18:
                    type = R34_EMAIL.day18;
                    break;
            }
        }

        // Follow up emails should only be send if session 8 was completed, and
        // the POST Session was not yet completed.
        if (s.completed(R34Study.NAME.SESSION8.toString()) && !s.completed(R34Study.NAME.POST.toString())) {
            switch (days) {
                case 60:
                    type = R34_EMAIL.followup;
                    break;
                case 63:
                    type = R34_EMAIL.followup2;
                    break;
                case 67:
                    type = R34_EMAIL.followup2;
                    break;
                case 70:
                    type = R34_EMAIL.followup2;
                    break;
                case 75:
                    type = R34_EMAIL.followup3;
                    break;
            }
        }

        return type;
    }




}
