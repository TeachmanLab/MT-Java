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

    @Override
    public String getName() {return "Templeton";}

    public TempletonStudy() {
        this.currentSession = "PreTest";
    }

    public TempletonStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session pretest, session1, session2;

        pretest = new Session ("PreTest", "Initial Assessment", 0, 0);
        pretest.addTask(new Task("Demographics","Demographics", Task.TYPE.questions, 2));
        pretest.addTask(new Task("MentalHealthHistory","Mental Health History", Task.TYPE.questions, 2));
        pretest.addTask(new Task("ExpectancyBias","Expectancy Bias", Task.TYPE.questions, 4));
        pretest.addTask(new Task("NGSES","Self Esteem Scale", Task.TYPE.questions, 2));
        pretest.addTask(new Task("Optimism","Optimism", Task.TYPE.questions, 3));
        pretest.addTask(new Task("AxImagery","Imagery", Task.TYPE.questions, 2));
        pretest.addTask(new Task("DASS21AS","Anxiety Subscale", Task.TYPE.questions, 2));
        pretest.addTask(new Task("DASS21DS","Depression Subscale", Task.TYPE.questions, 2));
        sessions.add(pretest);

        session1 = new Session("firstSession", "The First Session", 0, 2);
        session1.addTask(new Task("MyWebForm","Web Form", Task.TYPE.questions, 1));
        session1.addTask(new Task("Relatability","Relatability Follow Up", Task.TYPE.questions, 1));
        session1.addTask(new Task("RecognitionRatings", "Recognition Ratings", Task.TYPE.playerScript, 10));
        session1.addTask(new Task("TrainingExample", "A Basic Example", Task.TYPE.playerScript, 10));
        sessions.add(session1);

        session2 = new Session("secondSession", "The Second Session", 0, 0);
        session2.addTask(new Task("MyWebForm","Web Form", Task.TYPE.questions, 1));
        sessions.add(session2);

        return sessions;
    }
}
