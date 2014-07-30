package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.List;

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

    public enum SESSION_STATE {NOT_ELIGIBLE, READY, WAIT_A_DAY, WAIT_FOR_FOLLOWUP, ALL_DONE}

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String PASSWORD_MESSAGE = "Password must be 8 digits long.  It must contain one digit, a lower case letter, an upper case letter, and a special character.";

    @Size(min=2, max=100, message="Please specify your full name.")
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private boolean admin;

    @NotNull
    @Pattern(regexp=PASSWORD_REGEX, message = PASSWORD_MESSAGE)
    private String        password;
    @NotNull
    private String        passwordAgain;

    private List<Session> sessions  = Session.defaultList();
    private int           taskIndex;

    private boolean       emailOptout = false;  // User required to receive no more emails.

    private boolean       active = true;

    private Date          lastLoginDate;

    private Date          lastSessionDate;

    private List<EmailLog> emailLogs;

    private PasswordToken  passwordToken;

    public Participant() {}

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
     * Returns true if the session is completed, false otherwise.
     * @param session
     * @return
     */
    public boolean completed(Session.NAME session) {
        Session currentSession = getCurrentSession();
        if(session == currentSession.getName()) return false;
        for(Session.NAME s : Session.NAME.values()) {
            if (s.equals(session)) return true;
            if (s.equals(currentSession.getName())) return false;
        }
        // You can't really get here, since we have looped through all
        // the possible values of session.
        return false;
    }

    public boolean current(Session.NAME session) {
        if(session == getCurrentSession().getName()) return true;
        return false;
    }

    public void completeCurrentTask() {
        Session.NAME sessionName;

        // If this is the last task in a session, then we move to the next session.
        if(getTaskIndex() +1 == getCurrentSession().getTasks().size()) {
            this.taskIndex = 0;
            this.lastSessionDate = new Date();
            sessionName    = Session.nextSession(getCurrentSession().getName());
        } else { // otherwise we just increment the task index.
            this.taskIndex = taskIndex + 1;
            sessionName    = getCurrentSession().getName();
        }
        // Rebuid the session list, based on the now current session.
        this.sessions = Session.createListView(sessionName, taskIndex);
    }

    /**
     * @return Number of days since the last completed session.
     * Returns -1 if no sessions where ever completed.
     */
    public int daysSinceLastSession() {
        DateTime last;
        DateTime now;

        last = new DateTime(this.lastSessionDate);
        now  = new DateTime();
        return Days.daysBetween(last, now).getDays();
    }

    /**
     * Returns the state of the participant, in regards to the Session.  Can
     * be
     * @return
     */
    public SESSION_STATE sessionState() {
        if(daysSinceLastSession() == 0) return SESSION_STATE.WAIT_A_DAY;
        return SESSION_STATE.READY;
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

    public Session getCurrentSession() {
        for(Session s  : sessions) {
            if (s.isCurrent()) {
                return s;
            }
        }
        // If there is no current session, return the first session.
        sessions.get(0).setCurrent(true);
        return sessions.get(0);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        Session s = getCurrentSession();
        this.setTaskIndex(s.getCurrentTaskIndex());
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
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

    public Date getLastSessionDate() {
        return lastSessionDate;
    }

    public void setLastSessionDate(Date lastSessionDate) {
        this.lastSessionDate = lastSessionDate;
    }

    public PasswordToken getPasswordToken() {
        return passwordToken;
    }

    public void setPasswordToken(PasswordToken passwordToken) {
        this.passwordToken = passwordToken;
    }
}
