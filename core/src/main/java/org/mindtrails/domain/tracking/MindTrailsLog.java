package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.hasParticipant;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by dan on 2/13/17.
 * MindTrails logs a number of different events, this represents common
 * behavior across all logs.
 */
@MappedSuperclass
@Data
public abstract class MindTrailsLog implements Comparable<MindTrailsLog>, hasParticipant {

    @Id
    @GeneratedValue
    protected int id;

    @ManyToOne
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("participant")
    protected Participant participant;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="EEE, dd MMM yyyy HH:mm:ss Z", timezone="EST")
    protected Date dateSent;

    @Override
    public int compareTo(MindTrailsLog o) {
        if(this.dateSent == null) return 0;
        return this.dateSent.compareTo(o.dateSent);
    }

}
