package edu.virginia.psyc.r34.domain;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.tracking.TaskLog;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

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


    public PiParticipant() {
        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.createStudy(cbmCondition, CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    public PiParticipant(String fullName, String email, boolean admin) {
        super(fullName, email, admin);

        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.createStudy(cbmCondition, CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    /**
     * Sets the current study -
     * @param condition
     * @param session
     * @param taskIndex
     * @param lastSession
     * @param taskLogs
     */
    public void createStudy(CBM_CONDITION condition, String session, int taskIndex, Date lastSession, List<TaskLog> taskLogs) {
        if(condition == CBM_CONDITION.NEUTRAL) {
            this.study = new CBMNeutralStudy(session, taskIndex, lastSession, taskLogs, this.receiveGiftCards);
        } else {
            this.study = new CBMStudy(session, taskIndex, lastSession, taskLogs, this.receiveGiftCards);
        }
    }

    /**
     * Generates a Random CBM Condition from the list of conditions.
     */
    public static CBM_CONDITION randomCondition()  {
        List<CBM_CONDITION> values =
                Collections.unmodifiableList(Arrays.asList(CBM_CONDITION.values()));
        return values.get(RANDOM.nextInt(values.size()));
    }

    /**
     * Generates a Random Prime setting from the list of possible prime values.
     */
    public static PRIME randomPrime()  {
        List<PRIME> values =
                Collections.unmodifiableList(Arrays.asList(PRIME.values()));
        return values.get(RANDOM.nextInt(values.size()));
    }

    public boolean giftAwardedForSession(Session s) {
        for(GiftLog log : this.getGiftLogs()) {
            if (log.getSessionName().equals(s.getName())) {
                return true;
            }
        }
        return false;
    }

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
