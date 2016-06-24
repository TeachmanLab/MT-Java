package edu.virginia.psyc.r34.persistence;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * Basically a list of strings representing media shown to the participant..
 */
@Entity
@Table(name="data")
@Data
public class DataDAO {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("trialId")
    private TrialDAO trial;

    @Column(name="myKey")
    private String key;

    @Lob
    private String value;

    public DataDAO() {}

    public DataDAO(String k, String v, TrialDAO trial) {
        this.value = v;
        this.key   = k;
        this.trial = trial;
    }

}
