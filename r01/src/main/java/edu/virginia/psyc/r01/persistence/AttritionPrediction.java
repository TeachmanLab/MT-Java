package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    // Note that id and particpant id are the same.
    @Id
    protected Long id;
    @MapsId
    @OneToOne
    @JoinColumn(name = "participant_id")   //same name as id @Column
    protected Participant participant;
    @NotNull
    protected Date date_created;
    @NotNull
    protected Double confidence;
    @NotNull
    protected String version;


    public AttritionPrediction() {}

    public boolean isAtRisk() {
        if(confidence > 33) {
            return true;
        } else {
            return false;
        }
    }


}
