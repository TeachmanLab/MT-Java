package org.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.mindtrails.domain.tracking.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.hibernate.annotations.Parameter;
import javax.persistence.*;
import java.util.*;

/**
 * This class manages the storage and basic business logic
 * about a participant in a study.
 */
@Entity
@Table(name = "participant")
@Data
@EqualsAndHashCode(exclude={"emailLogs", "giftLogs", "SMSLogs", "passwordToken"})
public class Participant implements UserDetails {

    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);



//    @Id
//    @TableGenerator(name = "PARTICIPANT_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARTICIPANT_GEN")


    @Id
    @GenericGenerator(name = "PARTICIPANT_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @Parameter(name = "table_name", value = "ID_GEN"),
            @Parameter(name = "value_column_name", value = "GEN_VAL"),
            @Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @Parameter(name = "segment_value", value = "participant") })
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARTICIPANT_GEN")
    protected long id;
    protected String fullName;
    @Column(unique = true)
    protected String email;
    protected String phone;
    protected boolean admin;
    protected boolean testAccount;
    protected String password;
    protected boolean emailReminders = true;
    protected boolean phoneReminders = true;
    protected String timezone;
    protected boolean active = true;
    protected Date lastLoginDate;
    @JsonIgnore
    protected String randomToken;
    protected String theme = "blue";
    protected boolean over18;
    protected String reference; // The site the user came from when creating their account
    protected String campaign; // A key passed into the landing page, to help identify where people come from.
    protected boolean receiveGiftCards;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    protected PasswordToken passwordToken;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity=BaseStudy.class)
    protected Study study;

    // IMPORTANT: Automatic email notifications start failing when
    // these relationships are setup with a FetchType.LAZY. Please
    // leave this eager, or address that problem.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<EmailLog> emailLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<SMSLog> smsLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<GiftLog> giftLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<ErrorLog> errorLogs = new TreeSet<>();


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
        if (this.emailLogs == null) this.emailLogs = new TreeSet<>();
        this.emailLogs.add(log);
    }

    public void addSMSLog(SMSLog log) {
        if (this.smsLogs == null) this.smsLogs = new TreeSet<>();
        this.smsLogs.add(log);
    }

    public void addGiftLog(GiftLog log) {
        if (this.giftLogs == null) this.giftLogs =new TreeSet<>();
        this.giftLogs.add(log);
    }

    public void addErrorLog(ErrorLog log) {
        if (this.errorLogs == null) this.errorLogs = new TreeSet<>();
        this.errorLogs.add(log);
    }


    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", emailReminders=" + emailReminders +
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
        return daysSinceLastLog(emailLogs);
    }

    public int daysSinceLastSMSMessage() {
        return daysSinceLastLog(smsLogs);
    }

    private int daysSinceLastLog(Set<? extends MindTrailsLog> logs)  {

        DateTime last = null;
        DateTime each;
        DateTime now = new DateTime();

        if (null == logs || logs.size() == 0) return 99;

        for (MindTrailsLog log : logs) {
            each = new DateTime(log.getDateSent());
            if (null == last || last.isBefore(each)) {
                last = each;
            }
        }
        return Days.daysBetween(last, now).getDays();
    }


    /**
     * Returns the date of a last completed activity - this is the later date
     * of either the last login date or the last session date.
     */
    public Date lastMilestone() {
        if(this.study.getLastSessionDate() == null) {
            return this.lastLoginDate;
        }else if (this.study.getLastSessionDate().after(this.lastLoginDate)) {
            return this.study.getLastSessionDate();
        } else {
            return this.lastLoginDate;
        }
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

    /**
     * Checks to see if this type of email was already sent to the user regarding the
     * given session.
     */
    public boolean previouslySent(String type, String session) {
        for (EmailLog log : getEmailLogs()) {
            if (log.getEmailType().equals(type) &&
                    log.getSession().equals(session)) return true;
        }
        return false;
    }

    public boolean giftAwardedForSession(Session s) {
        for(GiftLog log : this.getGiftLogs()) {
            if (log.getSessionName().equals(s.getName())) {
                return true;
            }
        }
        return false;
    }

}

