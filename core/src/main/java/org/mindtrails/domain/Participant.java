package org.mindtrails.domain;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * This class manages the storage and basic business logic
 * about a participant in a study.
 */
@Entity
@Table(name = "participant")
@Data
public  class Participant implements UserDetails {

    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);


    @Id
    @TableGenerator(name = "PARTICIPANT_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARTICIPANT_GEN")
    protected long id;
    protected String fullName;
    @Column(unique = true)
    protected String email;
    protected boolean admin;
    protected String password;
    protected boolean emailOptout = false;
    protected boolean active = true;
    protected Date lastLoginDate;
    protected String randomToken;
    protected String theme = "blue";
    protected boolean over18;
    protected String reference; // The site the user came from when creating their account
    protected boolean receiveGiftCards;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected PasswordToken passwordToken;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity=BaseStudy.class)
    protected Study study;

    // IMPORTANT: Automatic email notifications start failing when
    // these relationships are setup with a FetchType.LAZY. Please
    // leave this eager, or address that problem.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    protected Collection<EmailLog> emailLogs = new ArrayList<EmailLog>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    protected Collection<GiftLog> giftLogs = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (admin) list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return list;
    }

    // Default no-arg constructor
    public Participant() {
    }

    // Utility to make testing easier.
    public Participant(String fullName, String email, boolean admin) {
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
    }

    /* ********************* *
     *  User Details Impl.   *
     * ********************* */

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

  /* ********************* *
     *  Logging.   *
     * ********************* */

    public void addEmailLog(EmailLog log) {
        if (this.emailLogs == null) this.emailLogs = new ArrayList<EmailLog>();
        this.emailLogs.add(log);
    }

    public void addGiftLog(GiftLog log) {
        if (this.giftLogs == null) this.giftLogs = new ArrayList<GiftLog>();
        this.giftLogs.add(log);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", emailOptout=" + emailOptout +
                ", currentSession=" + study.getCurrentSession() +
                ", taskIndex=" + study.getCurrentTaskIndex() +
                ", active=" + active +
                '}';
    }

    /* ********************* *
     *  Basic Functions.     *
     * ********************* */


    /**
     * Change the users password - this will take a plain text password
     * and run a one-way encyption on it for storage in the database.
     * Note that if the given password is null, it will not update the
     * filed.
     * @param password
     */
    public void updatePassword(String password) {
        if(password == null) return;
        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        this.password = hashedPassword;
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
        now = new DateTime();
        return Days.daysBetween(last, now).getDays();
    }


    public int daysSinceLastEmail() {
        DateTime last = null;
        DateTime each = null;
        DateTime now = new DateTime();

        if (null == this.emailLogs || this.emailLogs.size() == 0) return 99;

        for (EmailLog log : this.emailLogs) {
            each = new DateTime(log.getDateSent());
            if (null == last || last.isBefore(each)) {
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
        if (this.study.getLastSessionDate() != null) return this.study.getLastSessionDate();
        return this.lastLoginDate;
    }

    /**
     * Checks to see if this type of email was already sent to the user.
     */
    public boolean previouslySent(String type) {
        for (EmailLog log : getEmailLogs()) {
            if (log.getEmailType().equals(type)) return true;
        }
        return false;
    }

}

