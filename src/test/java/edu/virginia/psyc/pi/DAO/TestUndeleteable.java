package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.persistence.Questionnaire.QuestionnaireData;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dan on 10/26/15.
 */
@Entity
@Table(name="TestQuestionnaire")
@Data
@DoNotDelete
public class TestUndeleteable extends QuestionnaireData {

    private String value;

}
