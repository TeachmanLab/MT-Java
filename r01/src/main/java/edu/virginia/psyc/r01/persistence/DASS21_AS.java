package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    protected int dryness;
    @Column(name="BRE")
    protected int breathing;
    @Column(name="TRE")
    protected int trembling;
    @Column(name="WOR")
    protected int worry;
    @Column(name="PAN")
    protected int panic;
    @Column(name="HEA")
    protected int heart;
    @Column(name="SCA")
    protected int scared;
    @Column(name="over18")
    protected String over18 = "N/A";  // Only populated when used for eligibility
    protected String sessionId;


    public DASS21_AS() {}

    public DASS21_AS(int dryness, int breathing, int trembling, int worry, int panic, int heart, int scared) {
        this.dryness = dryness;
        this.breathing = breathing;
        this.trembling = trembling;
        this.worry = worry;
        this.panic = panic;
        this.heart = heart;
        this.scared = scared;
    }

    /**
     * Calculates the eligibility of a participant to be in a particular study
     * You take the average of the 7 item scores, and then multiply by 14
     * (we take the average, rather than the sum to account for missing data).
     * If the resulting # is 10 or higher, they are eligible to participate.
     * NOTE:  a "-1" indicates the question was not answered.
     */
    public double score() {
        int    sum   = 0;
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

