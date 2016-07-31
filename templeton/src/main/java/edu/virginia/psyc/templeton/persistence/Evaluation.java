package edu.virginia.psyc.templeton.persistence;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="MyWebForm")
@EqualsAndHashCode(callSuper = true)
@Data
public class Evaluation extends SecureQuestionnaireData {
    private boolean flowers;
}

