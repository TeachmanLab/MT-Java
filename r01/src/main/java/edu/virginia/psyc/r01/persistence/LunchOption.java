package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by saragomezt on 7/11/16.
 */

@Entity
@Table(name="LunchOption")
@EqualsAndHashCode(callSuper = true)
@Data
public class LunchOption extends LinkedQuestionnaireData {
    private int fire;
    private int spam;
    private int lightshow;

}