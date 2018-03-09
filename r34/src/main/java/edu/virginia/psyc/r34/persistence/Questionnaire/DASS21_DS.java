package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * The DASS21 Web form.
 */
@Entity
@Table(name="DASS21_DS")
@EqualsAndHashCode(callSuper = true)
@Data
public class DASS21_DS extends LinkedQuestionnaireData {

    private int nopositive;
    private int difficult;
    private int hopeless;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

}