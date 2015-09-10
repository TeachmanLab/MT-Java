package edu.virginia.psyc.pi.persistence.Questionnaire;

import javax.persistence.Entity;
import javax.persistence.Table;


import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="ImpactAnxiousImagery")
public class ImpactAnxiousImagery implements QuestionnaireData{

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int anxiety;
    private int vivd;
    private int badly;
    private int manageable;

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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getAnxiety() {
        return anxiety;
    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    public int getVivd() {
        return vivd;
    }

    public void setVivd(int vivd) {
        this.vivd = vivd;
    }

    public int getBadly() {
        return badly;
    }

    public void setBadly(int badly) {
        this.badly = badly;
    }

    public int getManageable() {
        return manageable;
    }

    public void setManageable(int terrible) {
        this.manageable = manageable;
    }
}
