package edu.virginia.psyc.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.*;
import edu.virginia.psyc.mindtrails.domain.DoNotDelete;
import edu.virginia.psyc.mindtrails.domain.Exportable;
import edu.virginia.psyc.mindtrails.domain.Participant;
import lombok.Data;

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

    public EmailLog() {};

    public EmailLog(Participant participant, String type) {
        this.participant = participant;
        this.emailType = type;
        this.dateSent  = new Date();
    }

    public EmailLog(Participant participant, String type, Date date) {
        this.participant = participant;
        this.emailType = type;
        this.dateSent = date;
    }

    @Override
    public int compareTo(EmailLog o) {
        return this.dateSent.compareTo(o.dateSent);
    }
}
