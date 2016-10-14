package edu.virginia.psyc.mobile;

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
@DiscriminatorValue("Mobile")
public class MobileStudy extends BaseStudy {


    @Override
    public String getName() {return "Mobile";}

    public MobileStudy() {
        this.currentSession = "PreTest";
    }

    public MobileStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session /*pretest,*/ session1, session2, session3, session4, session5 /*, post */;

//        pretest = new Session ("PreTest", "Initial Assessment", 0, 0);
//        pretest.addTask(new Task("Demographics","Demographics", Task.TYPE.questions, 2));
//        pretest.addTask(new Task("OA","Mood Assessment", Task.TYPE.questions,2));
//        pretest.addTask(new Task("RecognitionRating","Recognition Rating", Task.TYPE.jspsych, 2));
//        pretest.addTask(new Task("RR","Recognition Rating", Task.TYPE.questions, 2));
//        pretest.addTask(new Task("BBSIQ","Story evaluation", Task.TYPE.questions, 2));
//        pretest.setIndex(0);
//        sessions.add(pretest);

        session1 = new Session("firstSession", "The First Session", 0, 0);
        session1.addTask(new Task("trainingOne","Training Session One", Task.TYPE.jspsych, 2));
        session1.setIndex(1);
        sessions.add(session1);

        session2 = new Session("secondSession", "The Second Session", 0, 0);
        session2.addTask(new Task("trainingTwo","Training Session Two", Task.TYPE.jspsych, 2));
        session2.setIndex(2);
        sessions.add(session2);

        session3 = new Session("thirdSession", "The Third Session", 0, 0);
        session3.addTask(new Task("trainingThree","Training Session Three", Task.TYPE.jspsych, 2));
        session3.setIndex(3);
        sessions.add(session3);

        session4 = new Session("fourthSession", "The Fourth Session", 0, 0);
        session4.addTask(new Task("trainingFour","Training Session Four", Task.TYPE.jspsych, 2));
        session4.setIndex(4);
        sessions.add(session4);

        session5 = new Session("fifthSession", "The Fifth Session", 0, 0);
        session5.addTask(new Task("trainingFive","Training Session Five", Task.TYPE.jspsych, 2));
        session5.setIndex(5);
        sessions.add(session5);

//        post = new Session("PostFollowUp", "Follow Up", 0, 0);
//        post.addTask(new Task("OA","Mood Assessment", Task.TYPE.questions,2));
//        post.addTask(new Task("DASS21AS","My Feelings", Task.TYPE.questions, 2));
//        post.addTask(new Task("RecognitionRating","Recognition Rating", Task.TYPE.jspsych, 2));
//        post.addTask(new Task("RR","Recognition Rating", Task.TYPE.questions, 2));
//        post.addTask(new Task("BBSIQ","Story evaluation", Task.TYPE.questions, 2));
//        post.addTask(new Task("MultiUserExperience","User Experience", Task.TYPE.questions, 2));
//        post.setIndex(6);
//        sessions.add(post);

        return sessions;
    }
}
