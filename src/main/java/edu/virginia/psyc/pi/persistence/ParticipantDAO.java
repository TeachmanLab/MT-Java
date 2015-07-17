package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:41 PM
 * This is where the login information for participants is stored.
 * By implementing the UserDetails interface we are able to use this directly
 * to lookup and authenticate users within the Spring Security Framework.
 *
 */
@Entity
@Table(name="participant")
public class ParticipantDAO implements UserDetails {

    @Id
    @GeneratedValue
    private long id;
    private String fullName;

    @Column(unique=true)
    private String email;

    private String password;

    private boolean admin;

    private boolean emailOptout;

    private boolean active;

    private Date          lastLoginDate;

    private Date          lastSessionDate;

    private String        randomToken;


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordTokenDAO  passwordTokenDAO;

    @Enumerated(EnumType.STRING)
    private Participant.PRIME prime;

    @Enumerated(EnumType.STRING)
    private Participant.CBM_CONDITION cbmCondition;

    @Enumerated(EnumType.STRING)
    private Session.NAME currentSession = Session.NAME.values()[0]; // set to first session by default


    private int taskIndex = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<EmailLogDAO> emailLogDAOs = new ArrayList<EmailLogDAO>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<GiftLogDAO> giftLogDAOs = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(admin) list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return list;
    }

    // Default no-arg constructor
    public ParticipantDAO() {}

    // Utility to make testing easier.
    public ParticipantDAO(String fullName, String email, String password, boolean admin) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

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

    public void setPassword(String password) {
        this.password = password;
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

    public Session.NAME getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session.NAME currentSession) {
        this.currentSession = currentSession;
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

    public Collection<EmailLogDAO> getEmailLogDAOs() {
        return emailLogDAOs;
    }

    public void setEmailLogDAOs(Collection<EmailLogDAO> emailLogDAOs) {
        this.emailLogDAOs = emailLogDAOs;
    }

    public void addLog(EmailLogDAO log) {
        if(this.emailLogDAOs == null) this.emailLogDAOs = new ArrayList<EmailLogDAO>();
        this.emailLogDAOs.add(log);
    }

    public Collection<GiftLogDAO> getGiftLogDAOs() {
        return giftLogDAOs;
    }

    public void setGiftLogDAOs(Collection<GiftLogDAO> giftLogDAOs) {
        this.giftLogDAOs = giftLogDAOs;
    }

    public void addLog(GiftLogDAO log) {
        if(this.giftLogDAOs == null) this.giftLogDAOs = new ArrayList<GiftLogDAO>();
        this.giftLogDAOs.add(log);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public PasswordTokenDAO getPasswordTokenDAO() {
        return passwordTokenDAO;
    }

    public void setPasswordTokenDAO(PasswordTokenDAO passwordTokenDAO) {
        this.passwordTokenDAO = passwordTokenDAO;
    }

    public Participant.PRIME getPrime() {
        return prime;
    }

    public void setPrime(Participant.PRIME prime) {
        this.prime = prime;
    }

    public Participant.CBM_CONDITION getCbmCondition() {
        return cbmCondition;
    }

    public void setCbmCondition(Participant.CBM_CONDITION cbmCondition) {
        this.cbmCondition = cbmCondition;
    }

    @Override
    public String toString() {
        return "ParticipantDAO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", emailOptout=" + emailOptout +
                ", currentSession=" + currentSession +
                ", taskIndex=" + taskIndex +
                ", active=" + active +
                '}';
    }
}
