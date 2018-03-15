package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

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