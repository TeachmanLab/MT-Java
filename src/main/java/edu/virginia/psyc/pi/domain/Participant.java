package edu.virginia.psyc.pi.domain;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

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

    private List<Session> sessions;
    private int           taskIndex;

    public Participant() { }

    public Participant(long id, String fullName, String email, boolean admin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.admin = admin;
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
            sessionName    = Session.nextSession(getCurrentSession().getName());
        } else { // otherwise we just increment the task index.
            this.taskIndex = taskIndex + 1;
            sessionName    = getCurrentSession().getName();
        }
        // Rebuid the session list, based on the now current session.
        this.sessions = Session.createListView(sessionName, taskIndex);
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
        return null;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }
}
