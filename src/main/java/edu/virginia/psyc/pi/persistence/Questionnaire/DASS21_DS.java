package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import javax.persistence.*;

/**
 * The DASS21 Web form.
 */
@Entity
@Table(name="DASS21_DS")
@Data
public class DASS21_DS extends QuestionnaireData {

    private int nopositive;
    private int difficult;
    private int nervous;
    private int blue;
    private int noenthusiastic;
    private int noworth;
    private int meaningless;

}