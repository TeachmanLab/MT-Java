package org.mindtrails.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name="CoachPrompt")
@EqualsAndHashCode(callSuper = true)
@DoNotDelete
@Data
public class CoachPrompt extends LinkedQuestionnaireData {

    @Lob
    String technicalDifficulties;
    @Lob
    String difficultToUnderstand;
    @Lob
    String otherFeedback;

    @NotNull
    @MeasureField(desc="To what extent has the training helped you think about challenging or stressful\n" +
            "situations in your day-to-day life in different ways?")
    private Integer helped;

    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "Not at all");
        tmpScale.put(2, "Slightly");
        tmpScale.put(3, "Moderately");
        tmpScale.put(4, "Very");
        tmpScale.put(5, "Extremely");
        return Collections.unmodifiableMap(tmpScale);
    }
}
