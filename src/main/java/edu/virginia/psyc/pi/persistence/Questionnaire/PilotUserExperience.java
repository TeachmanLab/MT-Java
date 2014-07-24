package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="PilotUserExperience")
public class PilotUserExperience implements QuestionnaireData{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int ease;
    private int interest;
    private int like;
    private int look;
    private int privacy;
    private int understand;
    private int trust;
    private int practice;
    private int treat_anx;
    private int recommend;
    private int tiring_training;
    private int tiring_measures;
    private int continue_program;


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

    public int getEase() {
        return ease;
    }

    public void setEase(int ease) {
        this.ease = ease;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int getUnderstand() {
        return understand;
    }

    public void setUnderstand(int understand) {
        this.understand = understand;
    }

    public int getTrust() {
        return trust;
    }

    public void setTrust(int trust) {
        this.trust = trust;
    }

    public int getPractice() {
        return practice;
    }

    public void setPractice(int practice) {
        this.practice = practice;
    }

    public int getTreat_anx() {
        return treat_anx;
    }

    public void setTreat_anx(int treat_anx) {
        this.treat_anx = treat_anx;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getTiring_training() {
        return tiring_training;
    }

    public void setTiring_training(int tiring_training) {
        this.tiring_training = tiring_training;
    }

    public int getTiring_measures() {
        return tiring_measures;
    }

    public void setTiring_measures(int tiring_measures) {
        this.tiring_measures = tiring_measures;
    }

    public int getContinue_program() {
        return continue_program;
    }

    public void setContinue_program(int continue_program) {
        this.continue_program = continue_program;
    }

}
