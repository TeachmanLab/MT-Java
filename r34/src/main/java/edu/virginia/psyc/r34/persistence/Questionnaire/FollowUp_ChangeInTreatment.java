package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="FollowUp")
@EqualsAndHashCode(callSuper = true)
@Data
public class FollowUp_ChangeInTreatment extends LinkedQuestionnaireData {

    private int medication_tx;
    @Column(name="description")  // describe is a reserved word, and not a valid column name.
    private String describe;

}