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
@Table(name="DASS21_AS")
public class DASS21_AS implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int dryness;
    private int breathing;
    private int trembling;
    private int worry;
    private int panic;
    private int heart;
    private int scared;

    public DASS21_AS() {}

    public DASS21_AS(int dryness, int breathing, int trembling, int worry, int panic, int heart, int scared) {
        this.dryness = dryness;
        this.breathing = breathing;
        this.trembling = trembling;
        this.worry = worry;
        this.panic = panic;
        this.heart = heart;
        this.scared = scared;
    }


    /**
     * Calculates the eligibility of a participant to be in a particular study
     * You take the average of the 7 item scores, and then multiply by 14
     * (we take the average, rather than the sum to account for missing data).
     * If the resulting # is 10 or higher, they are eligible to participate.
     * NOTE:  a "-1" indicates the question was not answered.
     */
    public double score() {
        int    sum   = 0;
        double total = 0.0;

        if(dryness    >= 0) { sum += dryness; total++; }
        if(breathing  >= 0) { sum += breathing; total++; }
        if(trembling  >= 0) { sum += trembling; total++; }
        if(worry      >= 0) { sum += worry; total++; }
        if(panic      >= 0) { sum += panic; total++; }
        if(heart      >= 0) { sum += heart; total++; }
        if(scared     >= 0) { sum += scared; total++; }
        if(total == 0) return 0; // Avoid division by 0, the user has a 0 score.
        return((sum / total) * 14.0);
    }

    public boolean eligibleScore() {
        return(this.score() > 10);
    }

    public boolean atRisk(DASS21_AS original) {
        return (score() / original.score()) > 1.3;
    }

    /** Auto Generated methods follow */
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

    public int getDryness() {
        return dryness;
    }

    public void setDryness(int dryness) {
        this.dryness = dryness;
    }

    public int getBreathing() {
        return breathing;
    }

    public void setBreathing(int breathing) {
        this.breathing = breathing;
    }

    public int getTrembling() {
        return trembling;
    }

    public void setTrembling(int trembling) {
        this.trembling = trembling;
    }

    public int getWorry() {
        return worry;
    }

    public void setWorry(int worry) {
        this.worry = worry;
    }

    public int getPanic() {
        return panic;
    }

    public void setPanic(int panic) {
        this.panic = panic;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getScared() {
        return scared;
    }

    public void setScared(int scared) {
        this.scared = scared;
    }

    public Session.NAME getSession() {
        return session;
    }

    public void setSession(Session.NAME session) {
        this.session = session;
    }
}
