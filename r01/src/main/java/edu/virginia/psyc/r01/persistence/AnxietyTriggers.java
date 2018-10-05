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
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyTriggers")
@EqualsAndHashCode(callSuper = true)
@Data
public class AnxietyTriggers extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(desc="Anxiety at social gatherings (e.g., parties), speaking in public, when meeting new people or dating, or talking to an authority figure (e.g., boss, teacher)")
    private Integer social;
    @NotNull
    @MeasureField(desc="Fear about changes in bodily feelings (e.g., feeling dizzy, short of breath or rapid heart beat) or fears about having a 'panic attack'")
    private Integer sensations;
    @NotNull
    @MeasureField(desc="Fear about becoming anxious and not being able to leave a situation (e.g., getting stuck at a movie theater or in a crowd)")
    private Integer anxiousFear;
    @NotNull
    @MeasureField(desc="Extreme fear about a particular object or situation, like flying, being in a high place, certain insects or animals, or seeing blood or a wound")
    private Integer particularObject;
    @NotNull
    @MeasureField(desc="Anxiety about upsetting thoughts or images that keep coming to mind, or anxiety about rituals or activities you feel you have to do over and over again (e.g., checking things or washing your hands")
    private Integer thoughts;
    @NotNull
    @MeasureField(desc="Anxiety related to reminders of a prior traumatic experience (such as a natural disaster, accident, assault, or exposure to combat or violence)")
    private Integer priorTrauma;


    public Map<Integer, String> getScale() {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "Not at all");
        tmpScale.put(2, "Slightly");
        tmpScale.put(3, "Moderately");
        tmpScale.put(4, "Very");
        tmpScale.put(5, "Extremely");
        return Collections.unmodifiableMap(tmpScale);
    }
}

