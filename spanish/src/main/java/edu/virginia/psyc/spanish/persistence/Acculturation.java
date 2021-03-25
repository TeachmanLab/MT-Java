package edu.virginia.psyc.spanish.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name="Acculturation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Acculturation extends LinkedQuestionnaireData {

    @NotNull
    @MeasureField(order = 1, desc = "In general, what languages(s) do you read and speak?", group = "languages1")
    private Integer general;
    @NotNull
    @MeasureField(order = 2, desc = "What language(s) do you usually speak at home?", group = "languages2")
    private Integer home;
    @NotNull
    @MeasureField(order = 3, desc = "In which language(s) do you usually think?", group = "languages2")
    private Integer think;
    @NotNull
    @MeasureField(order = 4, desc = "What language(s) do you usually speak with your friends?", group = "languages2")
    private Integer speak;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        switch (group) {
            case ("languages1"):
                tmpScale.put(1, "acculturation.only_spanish");
                tmpScale.put(2, "acculturation.spanish_better_than_english");
                tmpScale.put(3, "acculturation.both_equally");
                tmpScale.put(4, "acculturation.english_better_than_spanish");
                tmpScale.put(5, "acculturation.only_english");
                break;
            case ("languages2"):
                tmpScale.put(1, "acculturation.only_spanish");
                tmpScale.put(2, "acculturation.more_spanish_than_english");
                tmpScale.put(3, "acculturation.both_equally");
                tmpScale.put(4, "acculturation.more_english_than_spanish");
                tmpScale.put(5, "acculturation.only_english");
                break;
        }
        return Collections.unmodifiableMap(tmpScale);
    }
}



