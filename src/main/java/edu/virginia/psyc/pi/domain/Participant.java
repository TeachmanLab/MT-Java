package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.service.EmailService;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class Participant {

    private long id;

    public enum CBM_CONDITION {FIFTY_FIFTY, POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}
    private static final Random RANDOM = new Random();  // For generating random CBM and Prime values.
    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);


    private String fullName;

    private String email;

    private boolean admin;

    private String         password;

    private boolean        emailOptout = false;  // User required to receive no more emails.

    private boolean        active = true;

    private boolean        increase30 = false;

    private Date           lastLoginDate;

    private List<EmailLog> emailLogs = new ArrayList<>();

    private List<GiftLog>  giftLogs = new ArrayList<>();

    private List<TaskLog>  taskLogs = new ArrayList<>();

    private PasswordToken  passwordToken;

    private CBM_CONDITION  cbmCondition;

    private PRIME          prime;

    private Study          study;

    private String theme = "blue";

    private boolean        over18;

    public Participant() {
        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.setStudy(cbmCondition, CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    public Participant(long id, String fullName, String email, boolean admin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
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

    /**
     * @return Number of days since the last completed session or if
     * no session was completed, the days since the last login.
     * Returns -1 if the user never logged in or completed a session.
     */
    public int daysSinceLastMilestone() {
        DateTime last;
        DateTime now;

        last = new DateTime(this.lastMilestone());
        now  = new DateTime();
        return Days.daysBetween(last, now).getDays();
    }


    public int daysSinceLastEmail() {
        DateTime last = null;
        DateTime each = null;
        DateTime now  = new DateTime();

        if(null == this.emailLogs || this.emailLogs.size() == 0) return 99;

        for(EmailLog log : this.emailLogs) {
            each = new DateTime(log.getDate());
            if(null == last || last.isBefore(each)) {
                last = each;
            }
        }

        return Days.daysBetween(last, now).getDays();
    }

    /**
     * Returns the date of a last completed activity - this is the last login
     * date if no sessions were ever completed.  It is the last session
     * completed date otherwise.  This is used as the basis for when to send
     * email messages.
     */
    public Date lastMilestone() {
        if(this.study.getLastSessionDate() != null) return this.study.getLastSessionDate();
        return this.lastLoginDate;
    }


    public void addEmailLog(EmailLog log) {
        if (this.emailLogs == null) emailLogs = new ArrayList<EmailLog>();
        emailLogs.add(log);
    }

    public void addGiftLog(GiftLog log) {
        if (this.giftLogs == null) giftLogs = new ArrayList<GiftLog>();
        giftLogs.add(log);
    }

    public boolean giftAwardedForSession(Session s) {
        for(GiftLog log : giftLogs) {
            if (log.getSessionName().equals(s.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addTaskLog(TaskLog log) {
        if (this.taskLogs == null) taskLogs = new ArrayList<TaskLog>();
        taskLogs.add(log);
    }

    /**
     * Checks to see if this type of email was already sent to the user.
     */
    public boolean previouslySent(EmailService.TYPE type) {
        for(EmailLog log : getEmailLogs()) {
            if (log.getType().equals(type)) return true;
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
