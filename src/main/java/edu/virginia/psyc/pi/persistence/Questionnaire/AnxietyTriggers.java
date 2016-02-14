package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.DoNotDelete;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.LoggerFactory;
import javax.persistence.*;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyTriggers")
@EqualsAndHashCode(callSuper = true)
@Data
public class AnxietyTriggers extends QuestionnaireData  {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AnxietyTriggers.class);

    private String howLong;
    private int social;
    private int sensations;
    private int worry;
    private int anxiousFear;
    private int particularObject;
    private int thoughts;
    private int priorTrauma;

}

