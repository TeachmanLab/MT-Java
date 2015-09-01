package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.CBMStudy;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Somewhat speicial questionnaire model, as it is used to gage eligibility.
 */
@Entity
@Table(name="DASS21_AS")
@Data
public class DASS21_AS implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int dryness;
    private int breathing;
    private int trembling;
    private int worry;
    private int panic;
    private int heart;
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

    public boolean atRisk(DASS21_AS original) {
        return (score() / original.score()) > 1.3;
    }

}

