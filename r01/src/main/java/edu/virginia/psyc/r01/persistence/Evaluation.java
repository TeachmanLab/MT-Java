package edu.virginia.psyc.r01.persistence;

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
@Table(name="Evaluation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Evaluation extends LinkedQuestionnaireData {

    @ElementCollection
    @CollectionTable(name = "evaluation_devices", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "devices")
    private List<String> devices;

    @NotNull
    @MeasureField(order = 1, desc = "How helpful did you find Calm Thinking for reducing your anxiety?", group = "block1")
    private Integer helpful;
    @NotNull
    @MeasureField(order = 2, desc = "How much did Calm Thinking improve your overall quality of life?", group = "block1")
    private Integer quality;
    @NotNull
    @MeasureField(order = 3, desc = "How much did Calm Thinking help improve your overall mood (e.g., feeling happier)?", group = "block1")
    private Integer overallMood;
    @NotNull
    @MeasureField(order = 4, desc = "How likely would you be to recommend Calm Thinking to others with similar anxiety difficulties?", group="block1")
    private Integer recommend;
    @NotNull
    @MeasureField(order = 5, desc = "How easy was Calm Thinking to use?", group = "block1")
    private Integer easy;
    @NotNull
    @MeasureField(order = 6, desc = "How much did Calm Thinking keep your interest and attention?", group = "block1")
    private Integer interest;
    @NotNull
    @MeasureField(order = 7, desc = "How much did you like Calm Thinking in general?", group = "block1")
    private Integer likeGral;
    @NotNull
    @MeasureField(order = 8, desc = "How much did you like the way Calm Thinking looked?", group = "block1")
    private Integer likedLooks;
    @NotNull
    @MeasureField(order = 9, desc = "How worried were you about your privacy in using Calm Thinking?", group = "block2")
    private Integer privacy;
    @NotNull
    @MeasureField(order = 10, desc = "How easy were the assessments to understand?", group = "block2")
    private Integer understandAssessment;
    @NotNull
    @MeasureField(order = 11, desc = "How easy was the training program (including instructions and materials) to understand?", group = "block2")
    private Integer understandTraining;
    @NotNull
    @MeasureField(order = 12, desc = "How much did you feel you could trust the information?", group = "block2")
    private Integer trustInfo;
    @NotNull
    @MeasureField(order = 13, desc = "How tiring did you find the training sessions?", group = "block2")
    private Integer tiringTraining;
    //bold "assessments"
    @NotNull
    @MeasureField(order = 14, desc = "How tiring did you find the assessments?", group = "block2")
    private Integer tiringAssessment;
    @NotNull
    @MeasureField(order = 15, desc = "On average, while you were completing the training sessions, how focused were you?", group = "block2")
    private Integer focused;
    @NotNull
    @MeasureField(order = 16, desc = "On average, while you were completing the training sessions, how  distracted were you?", group = "block2")
    private Integer distracted;

    @NotNull
    private Integer problems;

    private String problemsDesc;

    @NotNull
    private String idealSessions;
    @NotNull
    private Integer similarProgram;
    @NotNull
    private Integer otherTreatment;

    @ElementCollection
    @CollectionTable(name = "evaluation_places", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "places")
    private List<String> places;
    private String otherPlace;

    @Column(name="evalCondition") // 'Condition' is a reserved word in some databases.
    @NotNull
    private Integer condition;

    //This should only be @NotNull if condition is is 1, when the person thinks they were in the control group...
    private Integer whenCondition;

    @ElementCollection
    @CollectionTable(name = "evaluation_reasonsControl", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "reasonsControl")
    private List<String> reasonsControl;
    private String otherReasonControl;

    @ElementCollection
    @CollectionTable(name = "evaluation_howLearn", joinColumns= @JoinColumn(name = "id"))
    @Column(name = "howLearn")
    private List<String> howLearn;
    private String howLearnOtherLink;
    private String howLearnOther;

    @Override
    public Map<Integer, String> getScale(String group) {
        Map<Integer, String> tmpScale = new TreeMap<>();
            tmpScale.put(1, "Not at all");
            tmpScale.put(2, "Slightly");
            tmpScale.put(3, "Somewhat");
            tmpScale.put(4, "Mostly");
            tmpScale.put(5, "Very");

        return Collections.unmodifiableMap(tmpScale);
    }
}



