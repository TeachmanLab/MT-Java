package edu.virginia.psyc.pi.persistence.Questionnaire;

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
public class DASS21_DS extends QuestionnaireData {

    private int nopositive;
    private int difficult;
    private int hopeless;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

}