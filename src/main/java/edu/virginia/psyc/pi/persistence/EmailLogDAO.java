package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.*;
import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Exportable;
import edu.virginia.psyc.pi.service.EmailService;
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
public class EmailLogDAO implements Comparable<EmailLogDAO>{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participantId")
    private ParticipantDAO participantDAO;

    @Enumerated(EnumType.STRING)
    private EmailService.TYPE emailType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    private Date dateSent;

    public EmailLogDAO() {};

    public EmailLogDAO(ParticipantDAO participantDAO,  EmailService.TYPE type) {
        this.participantDAO = participantDAO;
        this.emailType = type;
        this.dateSent  = new Date();
    }

    @Override
    public int compareTo(EmailLogDAO o) {
        return this.dateSent.compareTo(o.dateSent);
    }
}
