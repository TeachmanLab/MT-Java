package edu.virginia.psyc.pi.persistence.Questionnaire;

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

    private int dryness;
    private int breathing;
    private int trembling;
    private int worry;
    private int panic;
    private int heart;
    private int scared;

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
}
