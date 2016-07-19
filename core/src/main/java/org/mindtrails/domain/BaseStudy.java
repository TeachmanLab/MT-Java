package org.mindtrails.domain;

import org.mindtrails.domain.RestExceptions.WaitException;
import org.mindtrails.domain.tracking.TaskLog;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.persistence.*;
import java.util.*;

/**
 * You can use this class to get some basic features
 * rather than implement everything in the Study interface.
 */
@Data
@Entity
@Table(name = "study")
@DiscriminatorColumn(name="studyType")
public abstract class BaseStudy implements Study {

    private static final Session NOT_STARTED  = new Session("NOT_STARTED", "Not Started", 0, 0, new ArrayList<Task>());
    private static final Session COMPLETE     = new Session("COMPLETE", "Complete", 0, 2, new ArrayList<Task>());

    @Id
    @TableGenerator(name = "STUDY_GEN", table = "ID_GEN", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "STUDY_GEN")
    protected long id;

    protected String currentSession;
    protected int currentTaskIndex = 0;
    protected Date lastSessionDate;
    protected boolean receiveGiftCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "study")
    protected Collection<TaskLog> taskLogs = new ArrayList<>();

    public BaseStudy() {}

    public BaseStudy(String currentName, int taskIndex, Date lastSessionDate, Collection<TaskLog> taskLogs, boolean receiveGiftCards) {
        this.currentSession = currentName;
        this.currentTaskIndex = taskIndex;
        this.lastSessionDate = lastSessionDate;
        this.taskLogs = new ArrayList<>();
        this.receiveGiftCards = receiveGiftCards;
    }

    @Override
    public List<Session> getSessions() {
        List<Session> sessions = getStatelessSessions();
        sessions.add(COMPLETE);
        // Add state to the sessions
        boolean complete = true;
        for(Session s : sessions) {
            if (s.getName().equals(currentSession)) {
                s.setCurrent(true);
                complete = false;
            }
            s.setComplete(complete);
            setTaskStates(s.getName(), s.getTasks(), currentTaskIndex);
        }

        return sessions;
    }


    /**
     * This should return a full list of sessions that define the study, but
     * does not need to mark them correctly with regards to being completed,
     * or inprogress.
     * @return
     */
    public abstract List<Session> getStatelessSessions();

    @Override
    public Session nextGiftSession() {
        boolean toCurrent = false;
        for (Session s : getSessions()) {
            if (toCurrent && s.getGiftAmount() > 0) return s;
            if (s.isCurrent()) toCurrent = true;
        }
        return null;
    }

    @Override
    public boolean isReceiveGiftCards() {
        return receiveGiftCards;
    }

    @Override
    public void setReceiveGiftCards(boolean value) {
        receiveGiftCards = value;
    }

    /**
     * @return Number of days since the last completed session.
     * Returns 99 if the user never logged in or completed a session.
     */
    public int daysSinceLastSession() {
        DateTime last;
        DateTime now;

        last = new DateTime(lastSessionDate);
        now  = new DateTime();
        return Days.daysBetween(last, now).getDays();
    }


    @Override
    public void completeCurrentTask() {
        // Log the completion of the task
        this.taskLogs.add(new TaskLog(this));

        if (getState().equals(STUDY_STATE.WAIT)){
            throw new WaitException();
        }

        // If this is the last task in a session, then we move to the next session.
        if(currentTaskIndex +1 >= getCurrentSession().getTasks().size()) {
            completeSession();
        } else { // otherwise we just increment the task index.
            this.currentTaskIndex = currentTaskIndex + 1;
        }
    }

    protected void completeSession() {
        List<Session> sessions = getSessions();
        boolean next = false;

        Session nextSession = getCurrentSession();
        for(Session s: sessions) {
            if(next == true) { nextSession = s; break; }
            if(s.isCurrent()) next = true;
        }

        this.currentTaskIndex = 0;
        this.currentSession = nextSession.getName();
        this.lastSessionDate = new Date();
    }


    @Override
    public Session getCurrentSession() {
        List<Session> sessions = getSessions();
        for(Session s  : getSessions()) {
            if (s.isCurrent()) {
                return s;
            }
        }
        // If there is no current session, return the first session.
        sessions.get(0).setCurrent(true);
        return sessions.get(0);
    }

    /**
     * Returns true if the session is completed, false otherwise.
     * @param session
     * @return
     */
    public boolean completed(String session) {
        Session currentSession = getCurrentSession();
        if(session == currentSession.getName()) return false;
        for(Session s : getSessions()) {
            String strName = s.getName();
            if (strName.equals(session)) return true;
            if (strName.equals(currentSession.getName())) return false;
        }
        // You can't really get here, since we have looped through all
        // the possible values of session.
        return false;
    }

    /**
     * This method churns through the list of tasks, setting the "current" and "complete" flags based on the
     * current task index. It also uses the task logs to determine the completion date.
     */
    protected void setTaskStates(String sessionName, List<Task> tasks, int taskIndex) {
        int index = 0;
        for (Task t : tasks) {
            t.setCurrent(taskIndex == index);
            t.setComplete(taskIndex > index);
            for(TaskLog log : taskLogs) {
                if (log.getSessionName().equals(sessionName) && log.getTaskName().equals(t.getName())) {
                    t.setDateCompleted(log.getDateCompleted());
                    break;
                }
            }
            index++;
        }
    }

    /**
     * May return null if there is no previous session.
     */
    @Override
    public Session getLastSession() {

        List<Session> sessions = getSessions();
        Session last = NOT_STARTED;

        for(Session s: sessions) {
            if (s.getName().equals(currentSession)) break;
            last = s;
        }
        return last;
    }

    @Override
    public void forceToSession(String sessionName) {
        this.currentSession = sessionName;
        this.currentTaskIndex = 0;
        this.lastSessionDate = null;
    }

    @Override
    public STUDY_STATE getState() {

        if (currentTaskIndex != 0) return STUDY_STATE.IN_PROGRESS;

        if (this.currentSession.equals(COMPLETE.getName())) {
            return STUDY_STATE.ALL_DONE;
        }

        if(daysSinceLastSession() < getCurrentSession().getDaysToWait()) {
            return STUDY_STATE.WAIT;
        }

        return STUDY_STATE.READY;
    }

    @Override
    public String toString() {
        return "BaseStudy{" +
                "id=" + id +
                ", currentSession='" + currentSession + '\'' +
                ", currentTaskIndex=" + currentTaskIndex +
                ", lastSessionDate=" + lastSessionDate +
                ", receiveGiftCards=" + receiveGiftCards +
                ", taskLogs=" + taskLogs +
                '}';
    }

    @Override
    public Map<String,String> getPiPlayerParameters(){
        Map<String,String> params = new HashMap<>();
        return params;
    }

}
