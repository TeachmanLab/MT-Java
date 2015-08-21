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
@Table(name="MentalHealthHxTx")
public class MentalHealthHxTx implements QuestionnaireData{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private String OtherDesc;
    private String OtherDescNo;

    @ElementCollection
    @CollectionTable(name="disorders", joinColumns=@JoinColumn(name="id"))
    @Column(name="disorder")
    private List<String> disorders;


    @ElementCollection
    @CollectionTable(name="Disorders_no", joinColumns=@JoinColumn(name="id"))
    @Column(name="Disorder_no")
    private List<String> disorders_no;



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

    public List<String> getDisorders() {
        return disorders;
    }

    public void setDisorders(List<String> disorders) {
        this.disorders = disorders;
    }

    public String getOtherDesc() {
        return OtherDesc;
    }

    public void setOtherDesc(String otherDesc) {
        OtherDesc = otherDesc;
    }

    public List<String> getDisorders_no() {
        return disorders_no;
    }

    public void setDisorders_no(List<String> disorders_no) {
        this.disorders_no = disorders_no;
    }

    public String getOtherDescNo() {
        return OtherDescNo;
    }

    public void setOtherDescNo(String otherDescNo) {
        OtherDescNo = otherDescNo;
    }

}

