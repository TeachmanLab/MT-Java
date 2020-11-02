package org.mindtrails.domain;


import org.springframework.mobile.device.Device;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Models a Study, and the participant's progress in that Study.
 */
public interface Study {

    enum STUDY_STATE {READY, IN_PROGRESS, WAIT, ALL_DONE}

    String getName(); // A human readable descriptive name for this Study.

    List<Session> getSessions();  // Returns all sessions, including the participants progress.

    List<String> getSessionNames();  // Returns a list of all the session names, in order.

    Session getLastSession(); // returns the last completed session .

    Session getCurrentSession(); // returns the current session.

    void forceToSession(String sessionName); // Forces the study to the beginning of session with the given name.

    Session getSession(String sessionName); // Returns a session object for the given session name.

    Session nextGiftSession(); // returns the current session.

    void completeCurrentTask(double timeOnTask, Device device, String userAgent); // completes a task, moving the participant further through the study/

    int getCurrentTaskIndex();  // Returns the index of the task currently underway within the current session.

    Date getLastSessionDate(); // Returns the date and time the participant completed the last session.

    Date getLastTaskDate(); // Returns the date and the the participant completed the last tasks

    void setLastSessionDate(Date date); // Sets the date and time the participant completed the last session.

    STUDY_STATE getState();  // returns the current state of the paricipant in the study.

    boolean completed(String sessionName); // returns true if the given session is completed.

    boolean isReceiveGiftCards();

    void setReceiveGiftCards(boolean value);

    boolean hasTask(String task);

    boolean isProgress(String task);

    String getConditioning();

    void setConditioning(String conditioning);

    String getStudyExtension();

    void setStudyExtension(String extension);

    List<String> getConditions();

    double getIncreasePercent();

    void setIncreasePercent(double value);


    /**
     *     Parameters to pass into the PIPlayer Script.  Frequently
     *     includes details about the current study, such as
     *     the participants Condition or Prime.
     */
    Map<String,Object> getPiPlayerParameters();

    long getParticipant();
}