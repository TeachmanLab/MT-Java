package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.service.EmailService;

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
public class EmailLogDAO {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;

    @Enumerated(EnumType.STRING)
    private EmailService.TYPE emailType;

    private Date dateSent;


    public EmailLogDAO() {};

    public EmailLogDAO(ParticipantDAO participantDAO,  EmailService.TYPE type) {
        this.participantDAO = participantDAO;
        this.emailType = type;
        this.dateSent  = new Date();
    }

    /****************************************
     *   Generated Methods follow
     ******************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParticipantDAO getParticipantDAO() {
        return participantDAO;
    }

    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    public EmailService.TYPE getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailService.TYPE emailType) {
        this.emailType = emailType;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
