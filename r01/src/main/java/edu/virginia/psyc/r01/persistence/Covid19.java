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
@Table(name="Covid19")
@EqualsAndHashCode(callSuper = true)
@Data
public class Covid19 extends LinkedQuestionnaireData {

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

    // 2. Please indicate if you have engaged in any of the following behaviors as a result of COVID-19.

    @NotNull
    @MeasureField(order = 1, desc = "Purchased more than usual of certain products I normally buy (e.g., toilet paper, canned goods)", group = "behaviors")
    private Integer productsMore;
    @NotNull
    @MeasureField(order = 2, desc = "Purchased additional products I don’t normally buy (e.g., disposable gloves, cleaning products)", group = "behaviors")
    private Integer productsAdditional;
    @NotNull
    @MeasureField(order = 3, desc = "Waited several days before touching items I’ve purchased", group = "behaviors")
    private Integer touching;
    @NotNull
    @MeasureField(order = 4, desc = "Avoided chores that require me to go outside (e.g., taking out the trash, checking the mail, walking the dog)", group = "behaviors")
    private Integer chores;
    @NotNull
    @MeasureField(order = 5, desc = "Used hand sanitizer or washed hands more frequently", group = "behaviors")
    private Integer sanitizer;
    @NotNull
    @MeasureField(order = 6, desc = "Cleaned my home or did laundry more than usual", group = "behaviors")
    private Integer laundry;
    @NotNull
    @MeasureField(order = 7, desc = "Checked in with friends, family, and romantic partners more than usual", group = "behaviors")
    private Integer checkedIn;
    @NotNull
    @MeasureField(order = 8, desc = "Practiced social distancing (i.e., stayed in my home as much as possible and limited interactions with people outside my home)", group = "behaviors")
    private Integer distancing;
    @NotNull
    @MeasureField(order = 9, desc = "It scares me when I am short of breath or coughing.", group = "symptoms")
    private Integer cough;
    @NotNull
    @MeasureField(order = 10, desc = "It scares me when others around me are short of breath or coughing.", group = "symptoms")
    private Integer coughOthers;
    @NotNull
    @MeasureField(order = 11, desc = "It scares me when I have a runny nose.", group = "symptoms")
    private Integer nose;
    @NotNull
    @MeasureField(order = 12, desc = "It scares me when others around me have a runny nose.", group = "symptoms")
    private Integer noseOthers;
    @NotNull
    @MeasureField(order = 13, desc = "I am worried about my COVID-19 risk.", group = "symptoms")
    private Integer worry;
    @NotNull
    @MeasureField(order = 14, desc = "I can control whether I get COVID-19.", group = "symptoms")
    private Integer control;
    @NotNull
    @MeasureField(order = 15, desc = "When I’m upset about COVID-19, I believe there is nothing I can do to make myself feel better.", group = "symptoms")
    private Integer upset;
    @NotNull
    @MeasureField(order = 16, desc = "I can control whether I get COVID-19.", group = "symptoms")
    private Integer test;
    @NotNull
    @MeasureField(order = 17, desc = "When I’m upset about COVID-19, I believe there is nothing I can do to make myself feel better.", group = "symptoms")
    private Integer test2;

    // 3. & 4. On an average day over the past 2 weeks, how much time per day in minutes did you spend reading news stories/social media about COVID-19?

    @NotNull
    private String news;
    @NotNull
    private String socialMedia;

    // 5. Based on criteria from the Center for Disease Control, are you at high risk for serious illness following infection of COVID-19?

    @NotNull
    private Integer highRisk;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(1, "  Very little");
        tmpScale.put(2, "");
        tmpScale.put(3, "Somewhat");
        tmpScale.put(4, "");
        tmpScale.put(5, "Very much");
        return Collections.unmodifiableMap(tmpScale);


    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("behaviors", "Please indicate if you have engaged in any of the following behaviors as a result of COVID-19.");
        desc.put("symptoms", "Please indicate how much you agree with the following statements:");
        return Collections.unmodifiableMap(desc);
    }
}
