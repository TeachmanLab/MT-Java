package edu.virginia.psyc.r34.MockClasses;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by dan on 10/26/15.
 */
@Entity
@Table(name="TestQuestionnaire")
@Data
@EqualsAndHashCode(callSuper=false)
public class TestQuestionnaire extends LinkedQuestionnaireData {

    private String value;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TestMultiValue", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "multiValue")
    private List<String> multiValue;

    public TestQuestionnaire() {}
    public TestQuestionnaire(String value) {
        this.value = value;
        this.date = new Date();
    }

}
