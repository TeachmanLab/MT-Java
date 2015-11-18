package edu.virginia.psyc.pi.persistence.Questionnaire;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.DoNotDelete;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
@DoNotDelete
public class DASS21_AS extends QuestionnaireData {

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    protected ParticipantDAO participantDAO;

    @Column(name="DRY")
    private int dryness;
    @Column(name="BRE")
    private int breathing;
    @Column(name="TRE")
    private int trembling;
    @Column(name="WOR")
    private int worry;
    @Column(name="PAN")
    private int panic;
    @Column(name="HEA")
    private int heart;
    @Column(name="SCA")
    private int scared;


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

    public boolean eligibleScore() {
        return(this.score() > 10 );
    }

}

