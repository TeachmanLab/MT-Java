package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="AUDIT")
public class AUDIT implements QuestionnaireData{


    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int drink_alc;
    private int drinks_freq;
    private int binge;
    private int cant_stop;
    private int fail;
    private int drink_morning;
    private int guilt;
    private int remembered;
    private int injured;
    private int friend;

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

    public int getDrink_alc() {
        return drink_alc;
    }

    public void setDrink_alc(int drink_alc) {
        this.drink_alc = drink_alc;
    }

    public int getDrinks_freq() {
        return drinks_freq;
    }

    public void setDrinks_freq(int drinks_freq) {
        this.drinks_freq = drinks_freq;
    }

    public int getBinge() {
        return binge;
    }

    public void setBinge(int binge) {
        this.binge = binge;
    }

    public int getCant_stop() {
        return cant_stop;
    }

    public void setCant_stop(int cant_stop) {
        this.cant_stop = cant_stop;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public int getDrink_morning() {
        return drink_morning;
    }

    public void setDrink_morning(int drink_morning) {
        this.drink_morning = drink_morning;
    }

    public int getGuilt() {
        return guilt;
    }

    public void setGuilt(int guilt) {
        this.guilt = guilt;
    }

    public int getRemembered() {
        return remembered;
    }

    public void setRemembered(int remembsered) {
        this.remembered = remembered;
    }

    public int getInjured() {
        return injured;
    }

    public void setInjured(int injured) {
        this.injured = injured;
    }

    public int getFriend() {
        return friend;
    }

    public void setFriend(int friend) {
        this.friend = friend;
    }
}
