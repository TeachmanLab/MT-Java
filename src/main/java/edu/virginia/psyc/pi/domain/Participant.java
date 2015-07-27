package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.service.EmailService;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class Participant {

    private long id;

    public enum CBM_CONDITION {FITFY_FIFTY, POSITIVE, NEUTRAL}
    public enum PRIME {NEUTRAL, ANXIETY}
    private static final Random RANDOM = new Random();  // For generating random CBM and Prime values.
    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);

    /**
    NOTE:  Recommend using stronger password security settings, like these:
    ------------------
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";
    **/
    public static final String PASSWORD_REGEX = "^.{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 8 digits long.";


    @Size(min=2, max=100, message="Please specify your full name.")
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private boolean admin;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String         password;
    @NotNull
    private String         passwordAgain;

    private boolean        emailOptout = false;  // User required to receive no more emails.

    private boolean        active = true;

    private Date           lastLoginDate;

    private List<EmailLog> emailLogs = new ArrayList<>();

    private List<GiftLog>  giftLogs = new ArrayList<>();

    private List<TaskLog>  taskLogs = new ArrayList<>();

    private PasswordToken  passwordToken;

    private CBM_CONDITION  cbmCondition;

    private PRIME          prime;

    private Study          study;


    public Participant() {
        cbmCondition = randomCondition();
        prime        = randomPrime();
        this.study = new CBMStudy(CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
    }

    public Participant(long id, String fullName, String email, boolean admin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
        cbmCondition = randomCondition();
        prime        = randomPrime();
       this.study = new CBMStudy(CBMStudy.NAME.ELIGIBLE.toString(), 0, null, new ArrayList<TaskLog>());
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
     * Checks to see if the given password matches some standard criteria:
     * @param password
     * @return
     */
    public static boolean validPassword(String password) {
        return password.matches(PASSWORD_REGEX);
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEmailOptout() {
        return emailOptout;
    }

    public void setEmailOptout(boolean emailOptout) {
        this.emailOptout = emailOptout;
    }

    public List<EmailLog> getEmailLogs() {
        return emailLogs;
    }

    public void setEmailLogs(List<EmailLog> emailLogs) {
        this.emailLogs = emailLogs;
    }

    public void addEmailLog(EmailLog log) {
        if (this.emailLogs == null) emailLogs = new ArrayList<EmailLog>();
        emailLogs.add(log);
    }

    public List<GiftLog> getGiftLogs() { return giftLogs; }

    public void setGiftLogs(List<GiftLog> giftLogs) { this.giftLogs = giftLogs; }

    public void addGiftLog(GiftLog log) {
        if (this.giftLogs == null) giftLogs = new ArrayList<GiftLog>();
        giftLogs.add(log);
    }

    public List<TaskLog> getTaskLogs() { return taskLogs; }

    public void setTaskLogs(List<TaskLog> taskLogs) { this.taskLogs = taskLogs; }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAgain() {
        return passwordAgain;
    }

    public void setPasswordAgain(String passwordAgain) {
        this.passwordAgain = passwordAgain;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public PasswordToken getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(PasswordToken passwordToken) {
        this.passwordToken = passwordToken;
    }

    public CBM_CONDITION getCbmCondition() {
        return cbmCondition;
    }

    public void setCbmCondition(CBM_CONDITION cbmCondition) {
        this.cbmCondition = cbmCondition;
    }

    public PRIME getPrime() {
        return prime;
    }

    public void setPrime(PRIME prime) {
        this.prime = prime;
    }

    public Study getStudy() {  return study;  }

    public void setStudy(Study study) { this.study = study;  }
}
