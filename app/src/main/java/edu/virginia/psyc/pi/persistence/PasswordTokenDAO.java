package edu.virginia.psyc.pi.persistence;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/29/14
 * Time: 3:15 PM
 * A token used to reset a password.  Only effective for a day.  Should be only one per participant.
 */
@Entity
@Table(name="PasswordToken")
public class PasswordTokenDAO {

    @Id
    @GeneratedValue
    private int     id;

    @OneToOne
    private ParticipantDAO participantDAO;
    private Date    dateCreated;
    private String  token;

    public PasswordTokenDAO(){}

    public PasswordTokenDAO(ParticipantDAO p, Date dateCreated, String token) {
        this.participantDAO = p;
        this.dateCreated = dateCreated;
        this.token = token;
    }

    public ParticipantDAO getParticipantDAO() {
        return participantDAO;
    }

    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
