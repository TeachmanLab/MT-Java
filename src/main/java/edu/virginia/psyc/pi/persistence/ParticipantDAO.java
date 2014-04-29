package edu.virginia.psyc.pi.persistence;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/26/14
 * Time: 10:41 PM
 * This is where the login information for participants is stored.
 * By implementing the UserDetails interface we are able to use this directly
 * to lookup and authenticate users.
 * This class is getting a bit convoluted and may need to get split up into
 * multple beans.  This is now a UserDetails object, a Form validator, and a
 * Data access object.
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> list = new ArrayList();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(admin) list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return list;
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


    @Override
    public String toString() {
        return "ParticipantDAO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + getAuthorities().toString() +
                '}';
    }
}
