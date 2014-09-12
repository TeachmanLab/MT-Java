package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MultiUserExperience")
public class MultiUserExperience implements QuestionnaireData{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Session.NAME session = Session.NAME.ELIGIBLE;

    private int helpful;
    private int qol;
    private int mood;
    private int recommend;
    private int ease;
    private int interest;
    @Column(name="like_value") // 'like' is a reserved word in the database syntax and can't be used as name.
    private int like;
    private int look;
    private int privacy;
    private int understand;
    private int training_ease;
    private int trust;
    private int internet;
    private int ideal;
    private int tiring_training;
    private int distracted;
    private int similar_program;
    private int other_therapy;
    @Column(name="where_value") // 'where' is a reserved word in the database syntax and can't be used as name.
    private int where;
    private String OtherDesc;
    private String cntrl_tx;

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

    public int getHelpful() {
        return helpful;
    }

    public void setHelpful(int helpful) {
        this.helpful = helpful;
    }

    public int getQol() {
        return qol;
    }

    public void setQol(int qol) {
        this.qol = qol;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
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

    public int getTraining_ease() {
        return training_ease;
    }

    public void setTraining_ease(int training_ease) {
        this.training_ease = training_ease;
    }

    public int getTrust() {
        return trust;
    }

    public void setTrust(int trust) {
        this.trust = trust;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public int getIdeal() {
        return ideal;
    }

    public void setIdeal(int ideal) {
        this.ideal = ideal;
    }

    public int getTiring_training() {
        return tiring_training;
    }

    public void setTiring_training(int tiring_training) {
        this.tiring_training = tiring_training;
    }

    public int getDistracted() {
        return distracted;
    }

    public void setDistracted(int distracted) {
        this.distracted = distracted;
    }

    public int getSimilar_program() {
        return similar_program;
    }

    public void setSimilar_program(int similar_program) {
        this.similar_program = similar_program;
    }

    public int getOther_therapy() {
        return other_therapy;
    }

    public void setOther_therapy(int other_therapy) {
        this.other_therapy = other_therapy;
    }

    public int getWhere() {
        return where;
    }

    public void setWhere(int where) {
        this.where = where;
    }

    public String getOtherDesc() {
        return OtherDesc;
    }

    public void setOtherDesc(String otherDesc) {
        OtherDesc = otherDesc;
    }

    public String getCntrl_tx() {
        return cntrl_tx;
    }

    public void setCntrl_tx(String cntrl_tx) {
        this.cntrl_tx = cntrl_tx;
    }
}
