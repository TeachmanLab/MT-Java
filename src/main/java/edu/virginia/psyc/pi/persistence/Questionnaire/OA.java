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
@Table(name="OA")
public class OA implements QuestionnaireData {


    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int anxious_freq;
    private int anxious_sev;
    private int avoid;
    private int interfere;
    private int interfere_social;

    public int getAnxious_freq() {
        return anxious_freq;
    }

    public void setAnxious_freq(int anxious_freq) {
        this.anxious_freq = anxious_freq;
    }

    public int getAnxious_sev() {
        return anxious_sev;
    }

    public void setAnxious_sev(int anxious_sev) {
        this.anxious_sev = anxious_sev;
    }

    public int getAvoid() {
        return avoid;
    }

    public void setAvoid(int avoid) {
        this.avoid = avoid;
    }

    public int getInterfere() {
        return interfere;
    }

    public void setInterfere(int interfere) {
        this.interfere = interfere;
    }

    public int getInterfere_social() {
        return interfere_social;
    }

    public void setInterfere_social(int interfere_social) {
        this.interfere_social = interfere_social;
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

   public String getSession() { return session; }

    public void setSession(String session) {this.session = session;}
}
