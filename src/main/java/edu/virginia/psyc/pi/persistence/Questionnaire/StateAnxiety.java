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
@Table(name="SA")
public class StateAnxiety implements QuestionnaireData{

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
    private int badly;
    private int terrible;
    private int vivid;

    private String situation_desc_2;
    private int think_feel_2;
    private int anxious_2;
    private int badly_2;
    private int terrible_2;
    private int vivid2;

    public int getVivid() {
        return vivid;
    }

    public void setVivid(int vivid) {
        this.vivid = vivid;
    }

    public int getVivid2() {
        return vivid2;
    }

    public void setVivid2(int vivid2) {
        this.vivid2 = vivid2;
    }
//    @ElementCollection
//    @CollectionTable(name="situations", joinColumns=@JoinColumn(name="id"))
//    @Column(name="situations")
//    private List<String> situations;


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

    public String getSituation_desc_2() {
        return situation_desc_2;
    }

    public void setSituation_desc_2(String situation_desc_2) {
        this.situation_desc_2 = situation_desc_2;
    }

    public int getThink_feel_2() {
        return think_feel_2;
    }

    public void setThink_feel_2(int think_feel_2) {
        this.think_feel_2 = think_feel_2;
    }

    public int getAnxious_2() {
        return anxious_2;
    }

    public void setAnxious_2(int anxious_2) {
        this.anxious_2 = anxious_2;
    }

    public int getBadly_2() {
        return badly_2;
    }

    public void setBadly_2(int badly_2) {
        this.badly_2 = badly_2;
    }

    public int getTerrible_2() {
        return terrible_2;
    }

    public void setTerrible_2(int terrible_2) {
        this.terrible_2 = terrible_2;
    }

//    public List<String> getSituations() {
//        return situations;
//    }
//
//    public void setSituations(List<String> situations) {
//        this.situations = situations;
//    }



}

