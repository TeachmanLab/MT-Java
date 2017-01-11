package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Exportable;
import org.mindtrails.domain.Participant;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs the date and time a particular type of email was sent to a user.
 */
@Entity
@Table(name="email_log")
@Exportable
@DoNotDelete
@Data
public class EmailLog implements Comparable<EmailLog> {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participantId")
    private Participant participant;

    private String emailType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    private Date dateSent;

    private boolean successful = true;
    private String exception = "";

    public EmailLog() {};

    public EmailLog(Email email) {
        this.participant = email.getParticipant();
        this.emailType = email.getType();
        this.dateSent  = new Date();
    }

    public EmailLog(Participant p, String type) {
        this.participant = p;
        this.emailType = type;
        this.dateSent  = new Date();
    }

    public EmailLog(Participant p, String type, Date date) {
        this.participant = p;
        this.emailType = type;
        this.dateSent  = date;
    }

    public void setError(Exception e) {
        this.successful = false;
        this.exception = e.getLocalizedMessage();
    }


    @Override
    public int compareTo(EmailLog o) {
        if(this.dateSent == null) return 0;
        return this.dateSent.compareTo(o.dateSent);
    }
}
