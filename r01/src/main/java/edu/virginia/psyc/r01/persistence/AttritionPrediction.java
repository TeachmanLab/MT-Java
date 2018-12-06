package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.*;
import java.util.Date;

/**
 * This table is populated by an external process that reads the database and uses
 * a complex model to predict the likelihood someone will drop out of the study early.
 * This data is used by the R01 study to randomly assign participants to a special
 * coaching condition.
 */
@Entity
@Table(name="AttritionPrediction")
@Data
public class AttritionPrediction {
    @Id
    protected Long id;
    @OneToOne
    protected Participant participant;
    protected Date date_created;
    protected Double likelihood;
    protected Double confidence;

    public AttritionPrediction() {}

    public AttritionPrediction(Participant p, Double likelihood, Double confidence) {
        this.id = p.getId();
        this.participant = p;
        this.likelihood = likelihood;
        this.confidence = confidence;
    }

    public boolean isAtRisk() {
        if(likelihood > 33) {
            return true;
        } else {
            return false;
        }
    }


}
