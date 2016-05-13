package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyTriggers")
@EqualsAndHashCode(callSuper = true)
@Data
public class AnxietyTriggers extends SecureQuestionnaireData {

    private String howLong;
    private int social;
    private int sensations;
    private int worry;
    private int anxiousFear;
    private int particularObject;
    private int thoughts;
    private int priorTrauma;

}

