package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="NGSES")
@EqualsAndHashCode(callSuper = true)
@Data
public class NGSES extends SecureQuestionnaireData {


    private int DifficultTasks;
    private int PerformEffectively;
    private int Compared;

    // The following items were removed,as three questions
    // were as much as we needed to get a statistically
    // significant output.
//    private int AchieveGoals;
//    private int ObtainOutcomes;
//    private int AnyEndeavor;
//    private int SuccessfullyOvercome;
//    private int ToughThings;

}

