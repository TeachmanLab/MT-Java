package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 5/26/14
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="Credibility")
@EqualsAndHashCode(callSuper = true)
@Data
public class Credibility extends LinkedQuestionnaireData {

    private int logical;
    private int important;
    private int recommendable;

}