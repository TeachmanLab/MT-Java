package edu.virginia.psyc.pi.domain;

import lombok.Data;
import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.Session;
import edu.virginia.psyc.mindtrails.domain.participant.GiftLog;
import edu.virginia.psyc.mindtrails.domain.participant.TaskLog;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 4/29/14
 * Time: 4:50 PM
 * This is used to create a new participant in the MVC login controller, and
 * for modifying participants in the admin interface.  And will be used to
 * reset passwords when that get's implemented.
 *
 * This is also used for displaying details about the Participant, and for housing
 * general business logic specific to the Participant.
 */
@Data
public class PiParticipant extends Participant {

    private long id;

    public enum CBM_CONDITION {FIFTY_FIFTY, POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}
    private boolean        increase30 = false;

    private CBM_CONDITION  cbmCondition;
    private PRIME          prime;

    public PiParticipant() {
        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.setStudy(cbmCondition, CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    public PiParticipant(long id, String fullName, String email, boolean admin) {
        super(id, fullName, email, admin);

        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.setStudy(cbmCondition, CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    /**
     * Sets the current study -
     * @param condition
     * @param session
     * @param taskIndex
     * @param lastSession
     * @param taskLogs
     */
    public void setStudy(CBM_CONDITION condition, String session, int taskIndex, Date lastSession, List<TaskLog> taskLogs) {
        if(condition == CBM_CONDITION.NEUTRAL) {
            this.study = new CBMNeutralStudy(session, taskIndex, lastSession, taskLogs);
        } else {
            this.study = new CBMStudy(session, taskIndex, lastSession, taskLogs);
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
        for(GiftLog log : giftLogs) {
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
