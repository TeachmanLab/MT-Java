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


@Entity
@Table(name="Mechanisms")
@EqualsAndHashCode(callSuper = true)
@Data
public class Mechanisms extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="I often look at a situation from different viewpoints.")
    private Integer cfiViewpoints;

    @NotNull
    @MeasureField(order=2, desc="I am willing to fully experience whatever thoughts, feelings, and sensations come up for me, without trying to change them.")
    private Integer compActWilling;

    @NotNull
    @MeasureField(order=3, desc="When I want to feel more positive emotion, I change the way I’m thinking about the situation.")
    private Integer erqPositive;

    @NotNull
    @MeasureField(order=4, desc="When I want to feel less negative emotion, I change the way I’m thinking about the situation.")
    private Integer erqNegative;

    @NotNull
    @MeasureField(order=5, desc="Unexpected events upset me greatly.")
    private Integer iusUnexpected;

    @NotNull
    @MeasureField(order=6, desc="When I am uncertain I can’t function very well.")
    private Integer iusUncertain;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Strongly disagree");
        tmpScale.put(1, "Disagree");
        tmpScale.put(2, "Somewhat disagree");
        tmpScale.put(3, "Neutral");
        tmpScale.put(4, "Somewhat agree");
        tmpScale.put(5, "Agree");
        tmpScale.put(6, "Strongly agree");
        return Collections.unmodifiableMap(tmpScale);
    }
}

