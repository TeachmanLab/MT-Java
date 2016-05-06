package edu.virginia.psyc.mindtrails.domain;

import edu.virginia.psyc.mindtrails.domain.participant.TaskLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A barebones implementation of Study to aid in testing other structures.
 */
public class TestStudy extends BaseStudy implements Study {


    public TestStudy(String currentName, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs) {
        super(currentName, taskIndex, lastSessionDate, taskLogs);
    }

    @Override
    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session(0, "SessionOne","Session One",false,false,0, testTasks()));
        sessions.add(new Session(1, "SessionTwo","Session Two",false,false,0, testTasks()));
        return sessions;
    }

    @Override
    public STUDY_STATE getState() {
        return STUDY_STATE.IN_PROGRESS;
    }

    private List<Task> testTasks() {
        return new ArrayList<>();
    }


}
