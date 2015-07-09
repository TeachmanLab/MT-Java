package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="StateAnxietyPost")
public class StateAnxietyPost implements QuestionnaireData{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private String situation_desc;
    private int think_feel;
    private int anxious;
    private int avoid;
    private int badly;
    private int terrible;

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

    public String getSituation_desc() {
        return situation_desc;
    }

    public void setSituation_desc(String situation_desc) {
        this.situation_desc = situation_desc;
    }

    public int getThink_feel() {
        return think_feel;
    }

    public void setThink_feel(int think_feel) {
        this.think_feel = think_feel;
    }

    public int getAnxious() {
        return anxious;
    }

    public void setAnxious(int anxious) {
        this.anxious = anxious;
    }

    public int getAvoid() {
        return avoid;
    }

    public void setAvoid(int avoid) {
        this.avoid = avoid;
    }

    public int getBadly() {
        return badly;
    }

    public void setBadly(int badly) {
        this.badly = badly;
    }

    public int getTerrible() {
        return terrible;
    }

    public void setTerrible(int terrible) {
        this.terrible = terrible;
    }
}

