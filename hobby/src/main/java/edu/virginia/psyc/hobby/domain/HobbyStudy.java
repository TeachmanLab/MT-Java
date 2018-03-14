package edu.virginia.psyc.hobby.domain;

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
import java.util.Map;

/**
 * This is where you define the sessions and tasks that make up the study.
 */
@Entity
@Data
@DiscriminatorValue("HOBBY")
public class HobbyStudy extends BaseStudy {


    public enum SESSION {firstSession, secondSession, thirdSession, fourthSession, fifthSession, sixthSession};

    public static final String FIRST_SESSION = "firstSession";
    public static final String SECOND_SESSION = "secondSession";
    public static final String THIRD_SESSION = "thirdSession";
    public static final String FOURTH_SESSION = "fourthSession";
    public static final String FIFTH_SESSION = "fifthSession";
    public static final String SIXTH_SESSION = "sixthSession";
    @Override
    public String getName() {return "Hobby";}

    public HobbyStudy() {
        this.currentSession = FIRST_SESSION;
    }

    public HobbyStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }


    @Override
    /** Check out the Templeton Study for building a more complex setup. */
    public Map<String,Object> getPiPlayerParameters() {
        Map<String,Object> map = super.getPiPlayerParameters();
        String sessionName = this.getCurrentSession().getName();
        map.put("lettersToRemove",1);
        map.put("sessionName", sessionName);
        switch(sessionName) {
            case FIRST_SESSION:
                map.put("lemon", true);
                map.put("lettersToRemove",1);
                break;
            case SECOND_SESSION:
                map.put("lemon", false);
                map.put("lettersToRemove",1);
                break;
            case THIRD_SESSION:
                map.put("lemon", false);
                map.put("lettersToRemove",1);
                break;
            case FOURTH_SESSION:
                map.put("lemon", false);
                map.put("lettersToRemove",2);
                break;
            case FIFTH_SESSION:
                map.put("lemon", false);
                map.put("lettersToRemove",2);
                break;
            case SIXTH_SESSION:
                map.put("lemon", false);
                map.put("lettersToRemove",2);
                break;
            default:  // This should only occur in testing / accessing admin etc...
                map.put("secondWordSet",true);
                map.put("lettersToRemove",1);
        }
        return map;
    }

    public List<String>getConditions(){
        return new ArrayList<>();
    }


    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session session1, session2, session3, session4, session5, session6;

        session1 = new Session(FIRST_SESSION, "Day 1", 0, 0);
        session1.setIndex(0);
        session1.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session1);

        session2 = new Session(SECOND_SESSION, "Day 2", 0, 1);
        session2.setIndex(1);
        session2.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session2);

        session3 = new Session(THIRD_SESSION, "Day 3", 0, 1);
        session3.setIndex(2);
        session3.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session3);

        session4 = new Session(FOURTH_SESSION, "Day 4", 0, 1);
        session4.setIndex(3);
        session4.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session4);

        session5 = new Session(FIFTH_SESSION, "Day 5", 0, 1);
        session5.setIndex(4);
        session5.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session5);

        session6 = new Session(SIXTH_SESSION, "Day 6", 0, 1);
        session6.setIndex(5);
        session6.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        sessions.add(session6);

        return sessions;

    }
}
