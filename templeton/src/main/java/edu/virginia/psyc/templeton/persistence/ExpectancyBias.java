package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.annotation.*;
import java.lang.reflect.Field;

@Entity
@Table(name="expectancy_bias")
@EqualsAndHashCode(callSuper = true)
@Data
@DoNotDelete
/**
 * Expectancy Bias Scoring
 * -----------------------
 Scoring: Expectancy bias is scored by subtracting the average of a participants’
 likelihood ratings (on a scale of 1 – 7) for negative events from their average
 for positive events (per Cabeleira et. al, 2014). A positive score indicates
 relatively positive interpretation bias, a negative score indicates relatively
 negative interpretation bias, and a score of 0 indicates not bias.
 Pre-selection Expectancy Bias Cut-off: 1.1111
 */
public class ExpectancyBias extends LinkedQuestionnaireData implements Comparable<ExpectancyBias>{

    public static int NO_ANSWER = -1;
    public static double MAX_ELIGIBLE_SCORE = 1.1111;
    public static double AT_RISK_DIFFERENCE = 1.5;

    // Urge to lie down
    @pos private int BetterRest;
    @nut private int Reruns;
    @neg private int VerySick;

    // Party
    @nut private int Bagel;
    @pos private int SettleIn;
    @neg private int Offend;

    // Performance Review
    @nut private int Lunch;
    @pos private int ConsideredAdvancement;
    @neg private int Stuck;

    // Stock Market
    @nut private int Thermostat;
    @pos private int FinanciallySecure;
    @neg private int Ruining;

    private String sessionId;

    /**
     * Uses the annotations present above (@pos, @neg, @nut) to generate
     * an average value for all the scores with that annotation.
     * @param type
     * @return
     */
    private double averageForType(Class<? extends Annotation> type) {
        int sum = 0;
        int value;
        double total = 0.0;

        for (Field field : this.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(type)) {
                    try {
                        value = (Integer) field.get(this);
                        if(value != NO_ANSWER) {
                            total ++;
                            sum += value;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        }
        return sum / total;
    }

    /** Returns the score, which is the difference between the average positive
     * answer and the average negative answer.
     * @return
     */
    public double positiveAverage() {return averageForType(pos.class);}
    public double negativeAverage() {return averageForType(neg.class);}
    public double score() {
        return(positiveAverage() - negativeAverage());
    }

    /**
     * Participants are eligible if their score is less than 1.1111.  Meaning they
     * are no more than a very minimal positive inclination.
     * @return
     */
    public boolean eligible() {
        return score() < MAX_ELIGIBLE_SCORE;
    }

    /** Determins if this score is low enough to consider the participant "at-risk"
        when compared to the original score.
     */
    public boolean atRisk(ExpectancyBias original) {
        return (score() / original.score()) > 1.5;
    }

    @Override
    public int compareTo(ExpectancyBias o) {
        return this.date.compareTo(o.getDate());
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use on fields only.
@interface pos {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use on fields only.
@interface neg {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //can use on fields only.
@interface nut {}
