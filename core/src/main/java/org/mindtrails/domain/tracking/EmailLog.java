package org.mindtrails.domain.tracking;

import lombok.Data;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Exportable;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Logs the date and time a particular type of email was sent to a user.
 */
@Entity
@Table(name="email_log")
@Exportable
@DoNotDelete
@Data
public class EmailLog extends MindTrailsLog {

    private String emailType;
    private boolean successful = true;
    private String exception = "";
    private String session;

    public EmailLog() {};

    public EmailLog(Email email) {
        this.participant = email.getParticipant();
        this.session = email.getParticipant().getStudy().getCurrentSession().getName();
        this.emailType = email.getType();
        this.dateSent  = new Date();
    }

    public EmailLog(Participant p, String type) {
        this.participant = p;
        this.session = p.getStudy().getCurrentSession().getName();
        this.emailType = type;
        this.dateSent  = new Date();
    }

    public EmailLog(Participant p, String type, Date date) {
        this.participant = p;
        this.session = p.getStudy().getCurrentSession().getName();
        this.emailType = type;
        this.dateSent  = date;
    }

    public void setError(Exception e) {
        this.successful = false;
        this.exception = e.getLocalizedMessage();
    }



}
