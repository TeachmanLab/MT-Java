package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="DASS21_DS")
public class DASS21_DS implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int nopositive;
    private int difficult;
    private int nervous;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Session.NAME getSession() {
        return session;
    }

    public void setSession(Session.NAME session) {
        this.session = session;
    }

    public int getNopositive() {
        return nopositive;
    }

    public void setNopositive(int nopositive) {
        this.nopositive = nopositive;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getNervous() {
        return nervous;
    }

    public void setNervous(int nervous) {
        this.nervous = nervous;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getNoenthusiastic() {
        return noenthusiastic;
    }

    public void setNoenthusiastic(int noenthusiastic) {
        this.noenthusiastic = noenthusiastic;
    }

    public int getNoworth() {
        return noworth;
    }

    public void setNoworth(int noworth) {
        this.noworth = noworth;
    }

    public int getMeaningless() {
        return meaningless;
    }

    public void setMeaningless(int meaningless) {
        this.meaningless = meaningless;
    }
}