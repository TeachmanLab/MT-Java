package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.map.HashedMap;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="CC")
@EqualsAndHashCode(callSuper = true)
@Data



public class CC extends LinkedQuestionnaireData {

    @MeasureField(order=1, desc="While reading the material in the training program, to what extent did you feel you could relate to the situations that were presented?")
    @NotNull
    private Integer related;

    @MeasureField(order=2, desc=" While reading the material in the training program, to what extent did you feel like it could be you behaving that way?")
    @NotNull
    private Integer compare;

    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "Not at all");
        tmpScale.put(2, "Slightly");
        tmpScale.put(3, "Somewhat");
        tmpScale.put(4, "Mostly");
        tmpScale.put(5, "Very much");
        return Collections.unmodifiableMap(tmpScale);
    }
}