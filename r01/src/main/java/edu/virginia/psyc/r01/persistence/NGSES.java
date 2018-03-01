package edu.virginia.psyc.r01.persistence;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

/**
 * Created by any on 7/29/17.
 */


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;


import javax.persistence.*;

@Entity
@Table(name="NGSES")
@EqualsAndHashCode(callSuper = true)
@Data



public class NGSES extends LinkedQuestionnaireData {
    private int achieve_goals;
    private int accomplish_tasks;
    private int obtain_outcomes;
    private int succeed_endeavor;
    private int overcome_challenges;
    private int perform_multitask;
    private int do_most;
    private int perform_tough;
}
