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


@Entity
@Table(name="comorbid")
@EqualsAndHashCode(callSuper = true)
@Data
public class Comorbid extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order=1, desc="Little interest or pleasure in doing things", group="bothered")
    private Integer pleasure;
    @NotNull
    @MeasureField(order=2, desc="Feeling down, depressed, or hopeless", group="bothered")
    private Integer depressed;
    @NotNull
    @MeasureField(order=3, desc="How often do you have a drink containing alcohol?", group="how_often")
    private Integer howOften;
    @NotNull
    @MeasureField(order=4, desc="How many drinks containing alcohol do you have on a typical day when you are drinking?", group="number")
    private Integer numberOfDrinks;
    @NotNull
    @MeasureField(order=5, desc="How often do you have six or more drinks on one occasion?", group="six_or_more")
    private Integer sixOrMore;

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("bothered", "Over the past two weeks, how often have you been bothered by any of the following problems?");
        desc.put("how_often", "How often do you have a drink containing alcohol?");
        desc.put("number", "");
        desc.put("six_or_seven", "");
        return Collections.unmodifiableMap(desc);
    }


    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case("bothered"):
                tmpScale.put(1, "Not at all");
                tmpScale.put(2, "Several days");
                tmpScale.put(3, "More than half of the days");
                tmpScale.put(4, "Nearly every day");
                break;
            case("how_often"):
                tmpScale.put(0, "Never");
                tmpScale.put(1, "Monthly or less");
                tmpScale.put(2, "2 to 4 times a month");
                tmpScale.put(3, "2 to 3 times a week");
                tmpScale.put(4, "4 or more times a week");
                break;
            case("number"):
                tmpScale.put(0, "0 to 2");
                tmpScale.put(1, "3 or 4");
                tmpScale.put(2, "5 or 6");
                tmpScale.put(3, "7 to 9");
                tmpScale.put(4, "10 or more");
                break;
            case("six_or_more"):
                tmpScale.put(0, "Never");
                tmpScale.put(1, "Less than monthly");
                tmpScale.put(2, "Monthly");
                tmpScale.put(3, "2 to 3 times per week");
                tmpScale.put(4, "2 or more times a week");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}

