package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name="TechnologyUse")
@EqualsAndHashCode(callSuper = true)
@Data
public class TechnologyUse extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="Laptop computer", group="techUse")
    private Integer laptop;
    @NotNull
    @MeasureField(order=2, desc="Desktop computer", group="techUse")
    private Integer desktop;
    @NotNull
    @MeasureField(order=3, desc="Tablet (iPad, Android tablet, etc.)", group="techUse")
    private Integer tablet;
    @NotNull
    @MeasureField(order=4, desc="Smartphone (iPhone, Android phone, etc.)", group="techUse")
    private Integer smartphone;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Not applicable");
        tmpScale.put(1, "0-2 hours per day");
        tmpScale.put(2, "2-4 hours per day");
        tmpScale.put(3, "More than 4 hours per day");

        return Collections.unmodifiableMap(tmpScale);
    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("techUse", "Please indicate how often, on average, you use the following devices:");
        return Collections.unmodifiableMap(desc);
    }

}
