package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MentalHealthHxTx")
@EqualsAndHashCode(callSuper = true)
@Data
public class MentalHealthHxTx extends QuestionnaireData {

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
    @CollectionTable(name = "helps_past", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "helps_past")
    private List<String> helps_past;


    private int psychiatrist = 0;
    private int psychologist = 0;
    private int school_counselor = 0;
    private int LMHC = 0;
    private int general_practitioner = 0;
    private int teacher = 0;
    private int family = 0;
    private int friend = 0;
    private int religious_leader = 0;
    private int coach = 0;
    private int book = 0;
    private int medicine = 0;
    private int online = 0;
    private int other = 0;

    private int psychiatrist_past = 0;
    private int psychologist_past = 0;
    private int school_counselor_past = 0;
    private int LMHC_past = 0;
    private int general_practitioner_past = 0;
    private int teacher_past = 0;
    private int family_past = 0;
    private int friend_past = 0;
    private int religious_leader_past = 0;
    private int coach_past = 0;
    private int book_past = 0;
    private int medicine_past = 0;
    private int online_past = 0;
    private int other_past = 0;


}

