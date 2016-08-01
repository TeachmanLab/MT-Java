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
    private int AchieveGoals;
    private int DifficultTasks;
    private int ObtainOutcomes;
    private int AnyEndeavor;
    private int SuccessfullyOvercome;
    private int PerformEffectively;
    private int Compared;
    private int ToughThings;

}

