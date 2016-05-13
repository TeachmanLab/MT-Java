package edu.virginia.psyc.mindtrails.MockClasses;


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
