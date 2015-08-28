package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MentalHealthHxTx")
@Data
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

    @ElementCollection
    @CollectionTable(name="helps", joinColumns=@JoinColumn(name="id"))
    @Column(name="helps")
    private List<String> helps;

    private int psychiatrist = -1;
    private int psychologist = -1;



}

