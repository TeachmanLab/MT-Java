package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyTriggers")
@EqualsAndHashCode(callSuper = true)
@Data
public class AnxietyTriggers extends LinkedQuestionnaireData {

    private String howLong;
    private int social;
    private int sensations;
    private int worry;
    private int anxiousFear;
    private int particularObject;
    private int thoughts;
    private int priorTrauma;

}

