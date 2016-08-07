package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="MentalHealthHistory")
@EqualsAndHashCode(callSuper = true)
@Data
public class MentalHealthHistory extends SecureQuestionnaireData {
    private String Other_Desc;
    private String Other_DescNo;
    private String Other_HelpCurrent;
    private String Other_HelpPast;
    private String Other_HelpReason;
    private String no_past_help_reason;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "Disorder")
    private List<String> Disorders;

    @ElementCollection
    @CollectionTable(name = "mental_health_disorders_past", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "PastDisorders")
    private List<String> PastDisorders;

    @ElementCollection
    @CollectionTable(name = "help", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "Help")
    private List<String> Help;

    @ElementCollection
    @CollectionTable(name = "helps_past", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "PastHelp")
    private List<String> PastHelp;


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
    private int app_past= 0;
    private int support_group_past=0;
    private int other_past = 0;


}


