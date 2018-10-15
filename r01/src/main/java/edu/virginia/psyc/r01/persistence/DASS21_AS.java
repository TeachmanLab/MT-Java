package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.map.HashedMap;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * This DASS21 questionnaire is used to gage eligibility in the study
 * This data should not be deleted after export, since there is a slight
 * risk that it would get exported between the time it is completed and the
 * moment when we determine eligibility.  Field names in the database are
 * obscured for this reason.
 */
@Entity
@Table(name="DASS21_AS")
@Data
@EqualsAndHashCode(callSuper = true)
@DoNotDelete
public class DASS21_AS extends LinkedQuestionnaireData {



    @Column(name="DRY")
    @MeasureField(desc="I was aware of dryness of my mouth.")
    @NotNull
    private Integer dryness;

    @Column(name="BRE")
    @MeasureField(desc="I experienced breathing difficulty (e.g., excessively rapid breathing, breathlessness in the absence of physical exertion).")
    @NotNull
    private Integer breathing;

    @Column(name="TRE")
    @MeasureField(desc="I experienced trembling (e.g., in the hands).")
    @NotNull
    private Integer trembling;

    @Column(name="WOR")
    @MeasureField(desc="I was worried about situations in which I might panic and make a fool of myself.")
    @NotNull
    private Integer worry;

    @Column(name="PAN")
    @MeasureField(desc="I felt I was close to panic.")
    @NotNull
    private Integer panic;

    @Column(name="HEA")
    @MeasureField(desc="I was aware of my heart's action in the absence of exercise (e.g., felt heart rate increase, heart missing a beat).")
    @NotNull
    private Integer heart;

    @Column(name="SCA")
    @MeasureField(desc="I felt scared without any good reason.")
    @NotNull
    private Integer scared;

    @Override
    public Map<Integer, String> getScale(String scale) {
        Map<Integer, String> tmpScale = new TreeMap<>();
        tmpScale.put(0, "Not at all");
        tmpScale.put(1, "Sometimes");
        tmpScale.put(3, "A lot of the time");
        tmpScale.put(4, "Most of the time");
        return Collections.unmodifiableMap(tmpScale);
    }

    @Override
    public Map<String, String> getGroupDescriptions() {
        Map<String, String> desc = new TreeMap<>();
        desc.put("", "Over the last week, how often have you been bothered by any of the following problems?");
        return Collections.unmodifiableMap(desc);
    }

    private String sessionId;

    public DASS21_AS() {}

    public DASS21_AS(Integer dryness, Integer breathing, Integer trembling, Integer worry, Integer panic, Integer heart, Integer scared) {
        this.dryness = dryness;
        this.breathing = breathing;
        this.trembling = trembling;
        this.worry = worry;
        this.panic = panic;
        this.heart = heart;
        this.scared = scared;
    }

    /**
     * Returns a new DASS21 data structure, where all the values are decremented by 1.
     * @return
     */
    public DASS21_AS decrementBy1() {
        DASS21_AS newDass = new DASS21_AS(
                this.dryness - 1,
                this.breathing - 1,
                this.trembling - 1,
                this.worry - 1,
                this.panic - 1,
                this.heart - 1,
                this.scared - 1);
        return newDass;
    }


    /**
     * Calculates the eligibility of a participant to be in a particular study
     * You take the average of the 7 item scores, and then multiply by 14
     * (we take the average, rather than the sum to account for missing data).
     * If the resulting # is 10 or higher, they are eligible to participate.
     * NOTE:  a "-1" indicates the question was not answered.
     */
    public double score() {
        Integer    sum   = 0;
        double total = 0.0;

        if(dryness    >= 0) { sum += dryness; total++; }
        if(breathing  >= 0) { sum += breathing; total++; }
        if(trembling  >= 0) { sum += trembling; total++; }
        if(worry      >= 0) { sum += worry; total++; }
        if(panic      >= 0) { sum += panic; total++; }
        if(heart      >= 0) { sum += heart; total++; }
        if(scared     >= 0) { sum += scared; total++; }
        if(total == 0) return 0; // Avoid division by 0, the user has a 0 score.
        return((sum / total) * 14.0);
    }

    public boolean eligible() {
        return(this.score() > 10 );
    }

}

