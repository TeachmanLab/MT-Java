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

    private int anx_dis;
    private String OtherDesc;
    private int not_anx_dis;
    private String NotOtherDesc;
    private int disorder_no;
    private String OtherDescNo;
    private int not_anx_disorder_no;


    @ElementCollection
    @CollectionTable(name="Not_anx_Disorders_no", joinColumns=@JoinColumn(name="id"))
    @Column(name="Not_anx_Disorder_no")
    private List<String> not_anx_disorders_no;


    @ElementCollection
    @CollectionTable(name="disorders", joinColumns=@JoinColumn(name="id"))
    @Column(name="disorder")
    private List<String> disorders;


    @ElementCollection
    @CollectionTable(name="Disorders_no", joinColumns=@JoinColumn(name="id"))
    @Column(name="Disorder_no")
    private List<String> disorders_no;


    @ElementCollection
    @CollectionTable(name="Not_anx_Disorders", joinColumns=@JoinColumn(name="id"))
    @Column(name="Not_anx_Disorder")
    private List<String> not_anx_disorders;


    public String getNotAnxOtherDescNo() {
        return NotAnxOtherDescNo;
    }

    public void setNotAnxOtherDescNo(String notAnxOtherDescNo) {
        NotAnxOtherDescNo = notAnxOtherDescNo;
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

    public int getAnx_dis() {
        return anx_dis;
    }

    public void setAnx_dis(int anx_dis) {
        this.anx_dis = anx_dis;
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

    public int getNot_anx_dis() {
        return not_anx_dis;
    }

    public void setNot_anx_dis(int not_anx_dis) {
        this.not_anx_dis = not_anx_dis;
    }

    public List<String> getNot_anx_disorders() {
        return not_anx_disorders;
    }

    public void setNot_anx_disorders(List<String> not_anx_disorders) {
        this.not_anx_disorders = not_anx_disorders;
    }

    public String getNotOtherDesc() {
        return NotOtherDesc;
    }

    public void setNotOtherDesc(String notOtherDesc) {
        NotOtherDesc = notOtherDesc;
    }

    public int getDisorder_no() {
        return disorder_no;
    }

    public void setDisorder_no(int disorder_no) {
        this.disorder_no = disorder_no;
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

    public int getNot_anx_disorder_no() {
        return not_anx_disorder_no;
    }

    public void setNot_anx_disorder_no(int not_anx_disorder_no) {
        this.not_anx_disorder_no = not_anx_disorder_no;
    }

    public List<String> getNot_anx_disorders_no() {
        return not_anx_disorders_no;
    }

    public void setNot_anx_disorders_no(List<String> not_anx_disorders_no) {
        this.not_anx_disorders_no = not_anx_disorders_no;
    }

    private String NotAnxOtherDescNo;
}

