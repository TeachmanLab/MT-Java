
package edu.virginia.psyc.r01.persistence;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

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
public class QOL extends SecureQuestionnaireData {

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
