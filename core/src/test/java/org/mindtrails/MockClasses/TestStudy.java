package org.mindtrails.MockClasses;


import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.Task;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * A barebones implementation of Study to aid in testing other structures.
 * There are two sessions each with two tasks.  There is a 2 day wait before
 * starting the second session.
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
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("SessionOne","Session One", 0, 0, testTasks(currentSession, currentTaskIndex)));
        sessions.add(new Session("SessionTwo","Session Two", 0, 2, testTasks(currentSession, currentTaskIndex)));
        return sessions;
    }


    private List<Task> testTasks(String session, int taskIndex) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("TestQuestionnaire", "Test Question", Task.TYPE.questions, 3));
        tasks.add(new Task("TestUndeleteable", "Test Undeleteable Question", Task.TYPE.questions, 3));
        return tasks;
    }


    @Override
    public String getName() {
        return "Test Study";
    }
}
