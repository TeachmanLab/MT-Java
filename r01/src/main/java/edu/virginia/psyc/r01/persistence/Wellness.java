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
@Table(name="Wellness")
@EqualsAndHashCode(callSuper = true)
@Data
public class Wellness extends LinkedQuestionnaireData {

    // Life Satisfaction
    @NotNull
    @MeasureField(order=1, desc="", group="satisfaction")
    Integer satisfaction;

    // NGSES
    @NotNull
    @MeasureField(order=2, desc="When facing difficult tasks, I am certain that I will accomplish them.", group="ngses")
    Integer accomplishTasks;
    @NotNull
    @MeasureField(order=3, desc="I am confident that I can perform well on many different tasks.", group="ngses")
    Integer performMultitask;
    @NotNull
    @MeasureField(order=4, desc="Compared to other people, I can do most tasks very well.", group="ngses")
    Integer doMost;

    // Growth Mindset
    @NotNull
    @MeasureField(order=5, desc="You can learn new things, but you can't really change how you think.", group="growth")
    Integer learn;
    @NotNull
    @MeasureField(order=6, desc="No matter how much you have been thinking a particular way, you can always change it quite a bit.", group="growth")
    Integer particularThinking;
    @NotNull
    @MeasureField(order=7, desc="You can always substantially change how you think.", group="growth")
    Integer alwaysChangeThinking;

    // LOT-R
    @NotNull
    @MeasureField(order=8, desc="If something can go wrong with me, it will.", group="lot-r")
    Integer wrongWill;
    @NotNull
    @MeasureField(order=9, desc="I hardly ever expect things to go my way.", group="lot-r")
    Integer hardlyEver;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("satisfaction", "All things considered, how satisfied are you with your life as a whole?");
        return Collections.unmodifiableMap(desc);
    }

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        if (group.equals("satisfaction")) {
            tmpScale.put(0, "Completely dissatisfied");
            for (int i = 1; i < 10; i++) {
                tmpScale.put(i, "");
            }
            tmpScale.put(10, "Completely satisfied");
        } else {
            tmpScale.put(0, "Strongly disagree");
            tmpScale.put(1, "Disagree");
            tmpScale.put(2, "Neutral");
            tmpScale.put(3, "Agree");
            tmpScale.put(4, "Strongly agree");
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}

