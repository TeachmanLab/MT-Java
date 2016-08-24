package edu.virginia.psyc.templeton.domain;

import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;

/**
 * A customized Participant for the Templeton Study, very simple
 * right now - just adding some basic conditions and primes.
 */
@Entity
@Data
public class TempletonParticipant extends Participant {

    public enum CONDITION {POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}

    private CONDITION      conditioning;
    private PRIME          prime;

}
