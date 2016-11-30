package org.mindtrails.domain.piPlayer;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 9:33 AM
 * Basically a list of strings representing media shown
 * to the participant.
 */
@Entity
@Table(name="media")
@Data
public class PiMedia {

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

    public PiMedia(String s, Trial trial) {
        this.trial = trial;
        this.value = s;
    }
}
