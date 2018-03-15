package edu.virginia.psyc.r34.persistence.Questionnaire;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 */
@Entity
@Table(name="QOL")
@EqualsAndHashCode(callSuper = true)
@Data
public class QOL extends LinkedQuestionnaireData {

    private int material;
    private int health;
    private int relationships;
    private int children;
    private int spouse;
    private int friend;
    private int helping;
    private int participating;
    private int learning;
    private int understanding;
    private int work;
    private int expression;
    private int socializing;
    private int reading;
    private int recreation;
    private int independence;

}
