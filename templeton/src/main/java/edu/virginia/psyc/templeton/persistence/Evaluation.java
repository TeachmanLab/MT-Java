package edu.virginia.psyc.templeton.persistence;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Evaluation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Evaluation extends SecureQuestionnaireData {
    private int Helpful;
    private boolean NoAns_Helpful;
    private int Quality;
    private boolean NoAns_Quality;
    private int OverallMood;
    private boolean NoAns_Mood;
    private int Recommend;
    private boolean NoAns_Recommend;
    private int Easy;
    private boolean NoAns_Easy;
    private int Interest;
    private boolean NoAns_Interest;
    private int LikeGral;
    private boolean NoAns_Like;
    private int likedLooks;
    private boolean NoAns_Looks;
    private int Privacy;
    private boolean NoAns_Privacy;
    private int UnderstandAssessment;
    private boolean NoAns_UnderstandAssessment;
    private int UnderstandTraining;
    private boolean NoAns_UnderstandTraining;
    private int TrustInfo;
    private boolean NoAns_TrustInfo;
    private int Problems;
    private boolean NoAns_Problems;
    private String IdealSessions;
    private String WhyIdeal;
    private int Tiring;
    private boolean NoAns_Tiring;
    private int Distracted;
    private boolean noAns_Distracted;
    private int Similar;
    private boolean NoAns_Similar;
    private int OtherTreatment;
    private boolean NoAns_OtherTreatment;
    private boolean Home;
    private boolean Work;
    private boolean Public;
    private boolean Commute;
    private boolean Vacation;
    private boolean OtherComplete;
    private String Other_Place;
    private int NoAns_where;
    private int Condition;


}

