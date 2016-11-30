package org.mindtrails.domain.piPlayer;

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
public class PiStimuli {

    @Id
    @GeneratedValue
    @JsonIgnore
    private int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("trialId")
    private Trial trial;

    @Lob
    private String value;

    public PiStimuli() {}

    public PiStimuli(String s, Trial trial) {
        this.trial = trial;
        this.value = s;
    }
}
