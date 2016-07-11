package edu.virginia.psyc.templeton.domain;

import lombok.Data;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Task;
import org.mindtrails.domain.tracking.TaskLog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is where you define the sessions and tasks that make up the study.
 */
@Entity
@Data
@DiscriminatorValue("Templeton")
public class TempletonStudy extends BaseStudy {

    // A Base Study has : currentSession, currentTaskIndex, lastSessionDate and receiveGiftCards

    public TempletonStudy() {}

    public TempletonStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    public static List<Session> studyDefinition() {
        List<Session> sessions = new ArrayList<>();
        Session session1, session2;

        session1 = new Session(0, "firstSession", "The First Session", 10);
        session1.addTask(new Task("MyWebForm","Web Form", Task.TYPE.questions, 1));
        sessions.add(session1);

        session2 = new Session(1, "secondSession", "The Second Session", 20);
        session1.addTask(new Task("MyWebForm","Web Form", Task.TYPE.questions, 1));
        sessions.add(session2);

        return sessions;
    }

    @Override
    public List<Session> getSessions() {
        List<Session> sessions = studyDefinition();

        boolean completed = true;
        boolean current = false;

        for(Session s : sessions) {
            if(s.getName().equals(currentSession)) {
                completed = false;
                current = true;
            }
            s.setComplete(completed);
            s.setCurrent(current);
            setTaskStates(s.getName(), s.getTasks(), currentTaskIndex);
            current = false;
        }
       return sessions;
    }

    @Override
    public STUDY_STATE getState() {
        // Otherwise it's time to start.
        return STUDY_STATE.IN_PROGRESS;
    }
}
