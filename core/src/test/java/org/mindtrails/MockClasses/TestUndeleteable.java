package org.mindtrails.MockClasses;

import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by dan on 10/26/15.
 */
@Entity
@Table(name="TestQuestionnaire")
@Data
@EqualsAndHashCode(callSuper=false)
@DoNotDelete
public class TestUndeleteable extends LinkedQuestionnaireData {

    private String value;

    public TestUndeleteable() {}
    public TestUndeleteable(String value) {
        this.value = value;
        this.date = new Date();
    }


}
