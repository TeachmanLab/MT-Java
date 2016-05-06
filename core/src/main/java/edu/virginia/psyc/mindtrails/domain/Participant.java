package edu.virginia.psyc.mindtrails.domain;

import edu.virginia.psyc.mindtrails.domain.participant.EmailLog;
import edu.virginia.psyc.mindtrails.domain.participant.GiftLog;
import edu.virginia.psyc.mindtrails.domain.participant.PasswordToken;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.virginia.psyc.mindtrails.domain.participant.TaskLog;

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
@Data
public class Participant {

    private long id;

    protected static final Random RANDOM = new Random();  // For generating random CBM and Prime values.
    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!#\\$@_'\\+\\?\\[\\]\\.\\- ])(?=.+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be at least 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";

    @Size(min=2, max=100, message="Please specify your full name.")
    protected String fullName;

    @Email
    @NotNull
    protected String email;

    @NotNull
    protected boolean admin;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    protected String         password;
    @NotNull
    protected String         passwordAgain;

    protected boolean        emailOptout = false;  // User required to receive no more emails.

    protected boolean        active = true;

    protected Date           lastLoginDate;

    protected List<EmailLog> emailLogs = new ArrayList<>();

    protected List<GiftLog>  giftLogs = new ArrayList<>();

    protected List<TaskLog>  taskLogs = new ArrayList<>();

    protected PasswordToken passwordToken;

    protected Study          study;

    protected String theme = "blue";

    protected boolean        over18;

    public Participant() {}

    public Participant(Study study) {
        this.study = study;
    }

    public Participant(long id, String fullName, String email, boolean admin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
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

    /**
     * Checks to see if this type of email was already sent to the user.
     */
    public boolean previouslySent(String type) {
        for(EmailLog log : getEmailLogs()) {
            if (log.getType().equals(type)) return true;
        }
        return false;
    }

    public void addEmailLog(EmailLog log) {
        if (this.emailLogs == null) emailLogs = new ArrayList<EmailLog>();
        emailLogs.add(log);
    }

    public void addGiftLog(GiftLog log) {
        if (this.giftLogs == null) giftLogs = new ArrayList<GiftLog>();
        giftLogs.add(log);
    }

    public void addTaskLog(TaskLog log) {
        if (this.taskLogs == null) taskLogs = new ArrayList<TaskLog>();
        taskLogs.add(log);
    }

}
