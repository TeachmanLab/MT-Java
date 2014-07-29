package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="ReasonsEnding")
public class ReasonEnding implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;

    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;



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
}
