package edu.virginia.psyc.r34.domain;

import lombok.Data;
import org.mindtrails.domain.Participant;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A customized Participant for the CBM Study.  Tracks CBM Condition and Prime settings,
 * and flags users that see an increase of 30% in their Oasis scores.
 */
@Entity
@Table(name = "participant")
@Data
public class PiParticipant extends Participant {

    public enum CBM_CONDITION {FIFTY_FIFTY, POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}

    private boolean        increase30 = false;
    private boolean        increase50 = false;
    private CBM_CONDITION  cbmCondition;
    private PRIME          prime;
    protected String       riskSession;  // The session that saw an increase in risk factor.


    /**
     * Returns true if the participant is in a session, versus being in a pre-assessment or
     * post assessment period.
     */
    public boolean inSession() {
        return (!this.getStudy().getCurrentSession().getName().equals(CBMStudy.NAME.PRE.toString()) &&
                !this.getStudy().getCurrentSession().getName().equals(CBMStudy.NAME.POST.toString()) &&
                !this.getStudy().getCurrentSession().getName().equals(CBMStudy.NAME.SESSION1.toString()));
    }

}
