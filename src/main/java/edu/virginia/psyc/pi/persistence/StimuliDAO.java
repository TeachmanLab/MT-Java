package edu.virginia.psyc.pi.persistence;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * A list of strings representing stimuli presented in a trialDAO.
 */
@Entity
@Table(name="stimuli")
@Data
public class StimuliDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("trialId")
    private TrialDAO trial;

    @Lob
    private String value;

    public StimuliDAO() {}

    public StimuliDAO(String s, TrialDAO trial) {
        this.trial = trial;
        this.value = s;
    }
}
