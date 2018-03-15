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
@Table(name="DD")
@EqualsAndHashCode(callSuper = true)
@Data
public class DD extends LinkedQuestionnaireData {

    private int monday_count;
    private int tuesday_count;
    private int wednesday_count;
    private int thursday_count;
    private int friday_count;
    private int saturday_count;
    private int sunday_count;

    private int monday_hours;
    private int tuesday_hours;
    private int wednesday_hours;
    private int thursday_hours;
    private int friday_hours;
    private int saturday_hours;
    private int sunday_hours;

    private int average_freq;
    private int average_amount;
    private boolean q1_noAns;
    private boolean q2_noAns;


}
