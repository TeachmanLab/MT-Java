package org.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.mindtrails.domain.data.DoNotDelete;
import org.mindtrails.domain.tracking.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.persistence.*;
import java.util.*;

/**
 * This class manages the storage and basic business logic
 * about a participant in a study.
 */
@Entity
@Table(name = "participant")
@Data
@EqualsAndHashCode(exclude={"emailLogs", "giftLogs", "SMSLogs", "passwordToken","verificationCode", "coachees"})
@DoNotDelete
public class Participant implements UserDetails, HasStudy {

    private static final Logger LOG = LoggerFactory.getLogger(Participant.class);

    @Id
    @GenericGenerator(name = "PARTICIPANT_GEN", strategy = "org.mindtrails.persistence.MindtrailsIdGenerator", parameters = {
            @Parameter(name = "table_name", value = "ID_GEN"),
            @Parameter(name = "value_column_name", value = "GEN_VAL"),
            @Parameter(name = "segment_column_name", value = "GEN_NAME"),
            @Parameter(name = "segment_value", value = "participant")})
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PARTICIPANT_GEN")
    protected long id;
    protected String fullName;
    @Column(unique = true)
    protected String email;
    protected String phone;
    protected boolean admin;
    protected boolean coaching;
    protected boolean testAccount;
    protected String password;
    protected boolean emailReminders = true;
    protected boolean phoneReminders = true;
    protected String timezone;
    protected boolean active = true;
    protected Date lastLoginDate;
    protected boolean wantsCoaching;
    protected String firstCoachingFormat;
    protected boolean receiveGiftCards = false;
    protected boolean verified = false;
    protected boolean blacklist = false;
    @JsonIgnore
    protected String randomToken;
    protected String theme = "blue";
    protected boolean over18;
    protected String reference; // The site the user came from when creating their account
    protected String campaign; // A key passed into the landing page, to help identify where people come from.
    protected Date returnDate; // Date this user plans to return for next session.
    protected float attritionRisk;  // percentage likelihood this person will leave the study early.
    protected boolean canTextMessage;  // can send and receive text messages
    protected String awardCountryCode = "US";
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date dateCreated = new Date();
    private String language = "en";  // Preferred language, should be either "en" or "es" for spanish

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    protected PasswordToken passwordToken;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    protected VerificationCode verificationCode;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity=BaseStudy.class)
    protected Study study;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity=Participant.class)
    @JoinColumn(name="coached_by_id")
    protected Participant coachedBy;

    @OneToMany(mappedBy="coachedBy")
    @JsonIgnore
    protected List<Participant> coachees = new ArrayList<>();


    // IMPORTANT: Automatic email notifications start failing when
    // these relationships are setup with a FetchType.LAZY. Please
    // leave this eager, or address that problem.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<EmailLog> emailLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<SMSLog> smsLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<GiftLog> giftLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateSent")
    protected SortedSet<ErrorLog> errorLogs = new TreeSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
    @JsonIgnore
    @OrderBy(value = "dateAttempted")
    protected SortedSet<CoachLog> coachLogs = new TreeSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (admin) list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (coaching) list.add(new SimpleGrantedAuthority("ROLE_COACH"));
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
     * Update the phone number
     */
    public void updatePhone(String phone){
        this.setPhone(formatPhone(phone));
    }

    public String formatPhone(String p) {
        String phoneLocale="US";
        if(p == null) return null;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phone = phoneUtil.parse(p, phoneLocale);
            return phoneUtil.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return p; // Leave it alone, let validation handle it.
        }
    }

    /**
     * Checks to see if this type of email was already sent to the user regarding the
     * given session.
     */


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

    public Locale locale() {
        if (this.getLanguage() != null) {
            return new Locale(this.getLanguage());
        } else {
            return Locale.ENGLISH;
        }
    }

    @JsonIgnore
    public int getTotalCoachInteractions() {
        return this.coachLogs.size();
    }


    @JsonIgnore
    public int getSuccessfulCoachInteractions() {
        int count = 0;
        for(CoachLog log : getCoachLogs()) {
            if(log.isSuccessful()) {
                count ++;
            }
        }
        return count;
    }

}

