package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
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
public class Credibility extends SecureQuestionnaireData {

    private int logical;
    private int important;
    private int recommendable;

}