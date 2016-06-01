package edu.virginia.psyc.pi.domain;

import java.util.Date;
import java.util.List;

/**
 * Models a Study, and the participant's progress in that Study.
 */
public interface Study {

    public enum STUDY_STATE {READY, IN_PROGRESS, WAIT_A_DAY, WAIT_FOR_FOLLOWUP, ALL_DONE}

    public List<Session> getSessions();  // Returns all sessions, including the participants progress.

    public Session getLastSession(); // returns the last completed session .

    public Session getCurrentSession(); // returns the current session.

    public Session nextGiftSession(); // returns the current session.

    public void completeCurrentTask(); // completes a task, moving the participant further through the study/

    public int getCurrentTaskIndex();  // Returns the index of the task currently underway within the current session.

    public Date getLastSessionDate(); // Returns the date and time the participant completed the last session.

    public void setLastSessionDate(Date date); // Sets the date and time the participant completed the last session.

    public STUDY_STATE getState();  // returns the current state of the paricipant in the study.

    public boolean completed(String sessionName); // returns true if the given session is completed.

    public boolean isReceiveGiftCards();

    public void setReceiveGiftCards(boolean value);

}