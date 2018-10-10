package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Evaluation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Evaluation extends LinkedQuestionnaireData {
    @NotNull
    private Integer helpful;
    @NotNull
    private Integer quality;
    @NotNull
    private Integer overallMood;
    @NotNull
    private Integer recommend;
    @NotNull
    private Integer easy;
    @NotNull
    private Integer interest;
    @NotNull
    private Integer likeGral;
    @NotNull
    private Integer likedLooks;
    @NotNull
    private Integer privacy;
    @NotNull
    private Integer understandAssessment;
    @NotNull
    private Integer understandTraining;
    @NotNull
    private Integer trustInfo;
    @NotNull
    private Integer problems;
    @NotNull
    private String idealSessions;
    @NotNull
    private String whyIdeal;
    @NotNull
    private Integer tiring;
    @NotNull
    private Integer distracted;
    @NotNull
    private Integer similarProgram;
    private Integer otherTreatment;

    private Boolean home;
    private Boolean work;
    private Boolean publicPlace;
    private Boolean commute;
    private Boolean vacation;
    private Boolean otherComplete;
    private String otherPlace;
    private Integer NoAnsWhere;
    @Column(name="evalCondition") // 'Condition' is a reserved word in some databases.
    @NotNull
    private Integer condition;


}

