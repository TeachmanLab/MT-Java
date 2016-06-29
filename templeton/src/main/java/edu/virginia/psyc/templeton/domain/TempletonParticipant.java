package edu.virginia.psyc.templeton.domain;

import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;

/**
 * A customized Participant for the CBM Study.  Tracks CBM Condition and Prime settings,
 * and flags users that see an increase of 30% in their Oasis scores.
 */
@Entity
@Data
public class TempletonParticipant extends Participant {

    public enum CONDITION {POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}

    private CONDITION      conditioning;
    private PRIME          prime;

}
