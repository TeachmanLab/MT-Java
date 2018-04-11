package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Wellness")
@EqualsAndHashCode(callSuper = true)
@Data
public class Wellness extends LinkedQuestionnaireData {

    // Life Satisfaction
    int satisfaction;

    // NGSES
    int accomplishTasks;
    int performMultitask;
    int doMost;

    // Growth Mindset
    int learn;
    int particularThinking;
    int alwaysChangeThinking;

    // LOT-R
    int wrongWill;
    int hardlyEver;

}

