package edu.virginia.psyc.pi.persistence;

import edu.virginia.psyc.pi.domain.PiParticipant;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Data
public class ParticipantDAO implements UserDetails {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantDAO.class);

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
    private boolean increase30;
    private Date          lastLoginDate;
    private Date          lastSessionDate;
    private String        randomToken;
    private String theme;
    private boolean over18;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PasswordTokenDAO  passwordTokenDAO;

    @Enumerated(EnumType.STRING)
    private PiParticipant.PRIME prime;

    @Enumerated(EnumType.STRING)
    private PiParticipant.CBM_CONDITION cbmCondition;

    private String currentSession; // set to first session by default

    private int taskIndex = 0;

    // IMPORTANT: Automatic email notifications start failing when
    // these relationships are setup with a FetchType.LAZY. Please
    // leave this eager, or address that problem.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participantDAO")
    private Collection<EmailLogDAO> emailLogDAOs = new ArrayList<EmailLogDAO>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participantDAO")
    private Collection<GiftLogDAO> giftLogDAOs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participantDAO")
    private Collection<TaskLogDAO> taskLogDAOs = new ArrayList<>();


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
    public ParticipantDAO(String fullName, String email, String password, boolean admin, String theme) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.theme = theme;
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

    public Collection<EmailLogDAO> getEmailLogDAOs() {
        return emailLogDAOs;
    }

    public void setEmailLogDAOs(Collection<EmailLogDAO> emailLogDAOs) {
        this.emailLogDAOs = emailLogDAOs;
    }

    public void addEmailLog(EmailLogDAO log) {
        if(this.emailLogDAOs == null) this.emailLogDAOs = new ArrayList<EmailLogDAO>();
        this.emailLogDAOs.add(log);
    }

    public Collection<GiftLogDAO> getGiftLogDAOs() {
        return giftLogDAOs;
    }

    public void setGiftLogDAOs(Collection<GiftLogDAO> giftLogDAOs) {
        this.giftLogDAOs = giftLogDAOs;
    }

    public void addGiftLog(GiftLogDAO log) {
        if(this.giftLogDAOs == null) this.giftLogDAOs = new ArrayList<GiftLogDAO>();
        this.giftLogDAOs.add(log);
    }

    public Collection<TaskLogDAO> getTaskLogDAOs() {
        return taskLogDAOs;
    }

    public void setTaskLogDAOs(Collection<TaskLogDAO> taskLogDAOs) {
        this.taskLogDAOs = taskLogDAOs;
    }

    public void addTaskLog(TaskLogDAO log) {
        if(this.taskLogDAOs == null) this.taskLogDAOs = new ArrayList<TaskLogDAO>();
        this.taskLogDAOs.add(log);
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
