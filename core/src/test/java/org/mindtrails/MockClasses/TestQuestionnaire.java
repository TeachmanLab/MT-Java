package org.mindtrails.MockClasses;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private double timeOnPage;

    public TestQuestionnaire() {}
    public TestQuestionnaire(String value, Participant participant) {
        this.value = value;
        this.date = new Date();
        this.participant = participant;

    }

    @Override
    public Map<String,Object> modelAttributes(Participant p) {
        Map<String,Object> atts = new HashMap<>();
        atts.put("test", "pickles");
        return atts;
    }

}
