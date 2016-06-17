package edu.virginia.psyc.pi.MockClasses;


import edu.virginia.psyc.mindtrails.domain.BaseStudy;
import edu.virginia.psyc.mindtrails.domain.Session;
import edu.virginia.psyc.mindtrails.domain.Study;
import edu.virginia.psyc.mindtrails.domain.Task;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * A barebones implementation of Study to aid in testing other structures.
 */
@Entity
@Data
@DiscriminatorValue("Test Study")
public class TestStudy extends BaseStudy implements Study {

    public TestStudy() {
        super();
        this.setCurrentSession("SessionOne");
    }

    public TestStudy(String sessionName, int index) {
        super();
        this.setCurrentSession(sessionName);
        this.setCurrentTaskIndex(index);
    }

    @Override
    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session(0, "SessionOne","Session One",false,false,0, testTasks(currentSession, currentTaskIndex)));
        sessions.add(new Session(1, "SessionTwo","Session Two",false,false,0, testTasks(currentSession, currentTaskIndex)));
        return sessions;
    }

    @Override
    public STUDY_STATE getState() {
        return STUDY_STATE.IN_PROGRESS;
    }

    private List<Task> testTasks(String session, int taskIndex) {

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("TestQuestionnaire", "Test Question", Task.TYPE.questions, 3));
        tasks.add(new Task("TestUndeleteable", "Test Undeleteable Question", Task.TYPE.questions, 3));
        tasks.add(new Task("OA", "OA", Task.TYPE.questions, 3));
        setTaskStates(session, tasks, taskIndex);
        return tasks;
    }


}
