package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="CC")
@EqualsAndHashCode(callSuper = true)
@Data
public class CC extends LinkedQuestionnaireData {

    private int related;
    private int compare;

}