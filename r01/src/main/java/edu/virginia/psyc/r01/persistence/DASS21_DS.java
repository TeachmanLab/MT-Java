package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The DASS21 Web form.
 */
@Entity
@Table(name="DASS21_DS")
@EqualsAndHashCode(callSuper = true)
@Data
public class DASS21_DS extends SecureQuestionnaireData {

    private int nopositive;
    private int difficult;
    private int hopeless;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

}