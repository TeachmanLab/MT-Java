package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name="Covid19")
@EqualsAndHashCode(callSuper = true)
@Data
public class Covid19 extends LinkedQuestionnaireData {

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("risk", "x");
        return Collections.unmodifiableMap(desc);
    }

    // 1. COVID-19 is affecting people in many different ways. Thinking about the past 2 weeks,
    // please indicate in what ways the following areas of your life have been affected as a result of COVID-19

    @NotNull
    private Integer work;
    @NotNull
    private Integer finances;
    @NotNull
    private Integer food;
    @NotNull
    private Integer childcare;
    @NotNull
    private Integer wellbeing;
    @NotNull
    private Integer physical;
    @NotNull
    private Integer healthcare;
    @NotNull
    private Integer productivity;
    @NotNull
    private Integer family;
    @NotNull
    private Integer partners;
    @NotNull
    private Integer friends;
    @NotNull
    private Integer sleep;
    @NotNull
    private Integer events;

    // Partially in MT Spanish
    // 2. Please indicate how much you agree with the following statements:

    @NotNull
    @MeasureField(desc = "I am worried about my COVID-19 risk.")
    private Integer worry;

    // 3. Based on criteria from the Center for Disease Control, are you at high risk for serious illness following infection of COVID-19?

    @NotNull
    private Integer highRisk;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "covid19.very_little");
        tmpScale.put(2, "x");
        tmpScale.put(3, "covid19.somewhat");
        tmpScale.put(4, "x");
        tmpScale.put(5, "covid19.very_much");
        return Collections.unmodifiableMap(tmpScale);

    }
        }


