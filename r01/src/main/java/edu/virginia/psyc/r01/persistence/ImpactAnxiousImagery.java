package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Table(name="ImpactAnxiousImagery")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImpactAnxiousImagery extends LinkedQuestionnaireData {

    @NotNull
    private Integer anxiety;

    @NotNull
    @MeasureField(order=1, desc="", group="vivid")
    private Integer vivid;

    @NotNull
    @MeasureField(order=2, desc="", group="badly")
    private Integer badly;

    @NotNull
    @MeasureField(order=3, desc="", group="manageable")
    private Integer manageable;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("vivid", "How vividly did you imagine the situation?");
        desc.put("badly", "How likely is it that this situation or task will turn out well vs. turn out badly?");
        desc.put("manageable", "If this situation did turn out badly, how well or badly would you be able to handle it?");
        return Collections.unmodifiableMap(desc);
    }

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case("vivid"):
                tmpScale.put(1, "Not at all vivid");
                tmpScale.put(2, "Somewhat vivid");
                tmpScale.put(3, "Moderately vivid");
                tmpScale.put(4, "Very vivid");
                tmpScale.put(5, "Totally vivid");
                break;
            case("badly"):
                tmpScale.put(1, "Very likely to turn out well");
                tmpScale.put(2, "Somewhat likely to turn out well");
                tmpScale.put(3, "Neutral");
                tmpScale.put(4, "Somewhat likely to turn out badly");
                tmpScale.put(5, "Very likely to turn out badly");
                break;
            case("manageable"):
                tmpScale.put(1, "Very well");
                tmpScale.put(2, "Somewhat well");
                tmpScale.put(3, "Neutral");
                tmpScale.put(4, "Somewhat badly");
                tmpScale.put(5, "Very badly");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }

}
