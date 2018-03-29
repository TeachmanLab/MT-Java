package edu.virginia.psyc.hobby.domain.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="Impact")
@EqualsAndHashCode(callSuper = true)
@Data
public class Impact extends LinkedQuestionnaireData {

    private int anxiety;
    private int vivid;
    private int badly;
    private int manageable;

}
