package edu.virginia.psyc.kaiser.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
        desc.put("symptoms", "Please indicate how much you agree with the following statements:");
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
    private Integer focus;
    @NotNull
    private Integer exercise;
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

    // 2. Please indicate how much you agree with the following statements:

    @NotNull
    @MeasureField(order = 1, desc = "I am worried about my COVID-19 risk.", group = "symptoms")
    private Integer worry;
    @NotNull
    @MeasureField(order = 2, desc = "I can control whether I get COVID-19.", group = "symptoms")
    private Integer control;

    // 3. Based on criteria from the Center for Disease Control, are you at high risk for serious illness following infection of COVID-19?

    @NotNull
    private Integer highRisk;

    // 4. Have you been diagnosed with COVID-19?

    @NotNull
    private Integer diagnosis;

    // 5. Have you gotten a test for COVID-19?

    @NotNull
    private Integer testCovid;

    private Integer testCovidResult;

    private Date testCovidDate;

    private Integer testCovidDateNoAnswer;

    // 6. Have you gotten an antibody test for COVID-19?

    @NotNull
    private Integer testAntibody;

    private Integer testAntibodyResult;

    private Date testAntibodyDate;

    private Integer testAntibodyDateNoAnswer;

    // 7. Do you know of a family member or close friend who has or had COVID-19?

    @NotNull
    private Integer covidKnow;

   // 8. Have you had symptoms consistent with COVID-19?

    @NotNull
    private Integer symptoms;

    private Date symptomsDate;

    private Integer symptomsDateNoAnswer;


    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "Very little");
        tmpScale.put(2, "");
        tmpScale.put(3, "Somewhat");
        tmpScale.put(4, "");
        tmpScale.put(5, "Very much");
        return Collections.unmodifiableMap(tmpScale);

    }

    }
