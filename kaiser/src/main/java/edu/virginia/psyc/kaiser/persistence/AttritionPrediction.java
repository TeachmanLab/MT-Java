package edu.virginia.psyc.kaiser.persistence;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    protected Long participantId;
    @NotNull
    protected Date dateCreated;
    @NotNull
    protected Double confidence;
    @NotNull
    protected String version;


    public AttritionPrediction() {}


}
