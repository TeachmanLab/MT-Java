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
public class MentalHealthHxTx implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private String OtherDesc;
    private String OtherDescNo;
    private String OtherHelp;
    private String OtherHelpPast;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "disorder")
    private List<String> disorders;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders_past", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "Disorder_past")
    private List<String> disorders_past;

    @ElementCollection
    @CollectionTable(name = "help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "help")
    private List<String> help;

    @ElementCollection
    @CollectionTable(name = "help_past", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "help_past")
    private List<String> help_past;

    private String psychiatrist;
    private int psychologist;
    private int school_counselor;
    private int LMHC;
    private int general_practitioner;
    private int teacher;
    private int family;
    private int friend;
    private int religious_leader;
    private int coach;
    private int book;
    private int medicine;
    private int online;
    private int other;

    private int psychiatrist_past;
    private int psychologist_past;
    private int school_counselor_past;
    private int LMHC_past;
    private int general_practitioner_past;
    private int teacher_past;
    private int family_past;
    private int friend_past;
    private int religious_leader_past;
    private int coach_past;
    private int book_past;
    private int medicine_past;
    private int online_past;
    private int other_past;


}

