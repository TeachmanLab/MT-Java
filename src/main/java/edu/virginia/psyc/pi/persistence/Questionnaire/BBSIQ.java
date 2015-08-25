package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="BBSIQ")
public class BBSIQ implements QuestionnaireData {


    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int visitors_outstay;
    private int visitors_engagement;
    private int visitors_bored;

    public int getVisitors_outstay() {
        return visitors_outstay;
    }

    public void setVisitors_outstay(int visitors_outstay) {
        this.visitors_outstay = visitors_outstay;
    }


    public int getVisitors_bored() {
        return visitors_bored;
    }

    public void setVisitors_bored(int visitors_bored) {
        this.visitors_bored = visitors_bored;
    }

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
}