package edu.virginia.psyc.pi.domain;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 4/29/14
 * Time: 4:50 PM
 * This is used to create a new participant in the MVC login controller.
 */
public class Participant {

    private long id;

    @Size(min=2, max=100)
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    private boolean admin;

    private Session.NAME currentSession;

    /**
     * Returns true if the session is completed, false otherwise.
     * @param session
     * @return
     */
    public boolean completed(Session.NAME session) {
        if(session == currentSession) return false;
        for(Session.NAME s : Session.NAME.values()) {
            if (s.equals(session)) return true;
            if (s.equals(currentSession)) return false;
        }
        // You can't really get here, since we have looped through all
        // the possible values of session.
        return false;
    }

    public boolean current(Session.NAME session) {
        if(session == currentSession) return true;
        return false;
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

    public Session.NAME getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session.NAME currentSession) {
        this.currentSession = currentSession;
    }
}
