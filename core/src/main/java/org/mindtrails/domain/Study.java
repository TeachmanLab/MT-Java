package org.mindtrails.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Models a Study, and the participant's progress in that Study.
 */
public interface Study {

    public enum STUDY_STATE {READY, IN_PROGRESS, WAIT, ALL_DONE}

    public String getName(); // A human readable descriptive name for this Study.

    public List<Session> getSessions();  // Returns all sessions, including the participants progress.

    public Session getLastSession(); // returns the last completed session .

    public Session getCurrentSession(); // returns the current session.

    public void forceToSession(String sessionName); // Forces the study to the beginning of session with the given name.

    public Session getSession(String sessionName); // Returns a session object for the given session name.

    public Session nextGiftSession(); // returns the current session.

    public void completeCurrentTask(double timeOnTask); // completes a task, moving the participant further through the study/

    public int getCurrentTaskIndex();  // Returns the index of the task currently underway within the current session.

    public Date getLastSessionDate(); // Returns the date and time the participant completed the last session.

    public Date getLastTaskDate(); // Returns the date and the the participant completed the last tasks

    public void setLastSessionDate(Date date); // Sets the date and time the participant completed the last session.

    public STUDY_STATE getState();  // returns the current state of the paricipant in the study.

    public boolean completed(String sessionName); // returns true if the given session is completed.

    public boolean isReceiveGiftCards();

    public void setReceiveGiftCards(boolean value);

    public long getParticipantId();

    public boolean hasTask(String task);

    public String getConditioning();

    public List<String> getConditions();

    /**
     *     Parameters to pass into the PIPlayer Script.  Frequently
     *     includes details about the current study, such as
     *     the participants Condition or Prime.
     */
    public Map<String,Object> getPiPlayerParameters();
}