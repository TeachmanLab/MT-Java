package edu.virginia.psyc.r01.persistence;

/**
 * Created by Diheng on 4/27/17.
 */

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

@Entity
@Table(name="Credibility")
@EqualsAndHashCode(callSuper = true)
@Data
public class Credibility extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="How important is reducing your anxiety to you right now?")
    private Integer important;
    @NotNull
    @MeasureField(order=2, desc="How confident are you that an online training program will reduce your anxiety?")
    private Integer confident_online;

    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Not at all");
        tmpScale.put(1, "A little");
        tmpScale.put(2, "Somewhat");
        tmpScale.put(3, "A lot");
        tmpScale.put(4, "Very");
        return Collections.unmodifiableMap(tmpScale);
    }

}

