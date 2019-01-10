package edu.virginia.psyc.r01.domain;

import edu.virginia.psyc.r01.persistence.DASS21_AS;
import lombok.Data;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Task;
import org.mindtrails.domain.tracking.TaskLog;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is where you define the sessions and tasks that make up the study.
 */
@Entity
@Data
@DiscriminatorValue("R01")
public class R01Study extends BaseStudy {

    // Computer - CBM-I training delivered by computer
    // Mobile - CBM-I training delivered by mobile phone
    // Neutral - Control Condition with alternate content
    public enum CONDITION {NONE, TRAINING, CONTROL, LR_TRAINING, HR_COACH, HR_NO_COACH}

    public enum SESSION {preTest, firstSession, secondSession, thirdSession, fourthSession, fifthSession, PostFollowUp}

    public static final String PRE_TEST = "preTest";
    public static final String FIRST_SESSION = "firstSession";
    public static final String SECOND_SESSION = "secondSession";
    public static final String THIRD_SESSION = "thirdSession";
    public static final String FOURTH_SESSION = "fourthSession";
    public static final String FIFTH_SESSION = "fifthSession";
    public static final String POST_FOLLOWUP = "PostFollowUp";

    @Override
    public String getName() {return "R01";}

    public R01Study() {
        this.currentSession = PRE_TEST;
    }

    public R01Study(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    public void completeEligibility(DASS21_AS dass) {
        TaskLog t = new TaskLog();
        t.setDateCompleted(dass.getDate());
        t.setTaskName("DASS21_AS");
        t.setSessionName("Eligibility");
        t.setStudy(this);
        taskLogs.add(t);
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
            default:  // This should only occur in testing / accessing admin etc...
                map.put("secondWordSet",true);
                map.put("lettersToRemove",1);
        }
        return map;
    }

    public List<String>getConditions(){
        return Stream.of(CONDITION.values()) .map(Enum::name) .collect(Collectors.toList());
    }

    public boolean inSession() {
        return (!getCurrentSession().getName().equals(R01Study.SESSION.preTest.toString()) &&
                !getCurrentSession().getName().equals(R01Study.SESSION.firstSession.toString()) &&
                !getCurrentSession().getName().equals(R01Study.SESSION.secondSession.toString()) &&
                !getCurrentSession().getName().equals(R01Study.SESSION.thirdSession.toString())&&
                !getCurrentSession().getName().equals(R01Study.SESSION.fourthSession.toString())&&
                !getCurrentSession().getName().equals(R01Study.SESSION.fifthSession.toString())&&
                !getCurrentSession().getName().equals(R01Study.SESSION.PostFollowUp.toString()));

    }


    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session pretest, session1, session2, session3, session4, session5, post;

        pretest = new Session (PRE_TEST, "Initial Assessment", 500, 0);
        pretest.setIndex(0);
        pretest.addTask(new Task("Credibility","How Does Calm Thinking Work?", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("Demographics","Demographics", Task.TYPE.questions, 2 ));
        pretest.addTask(new Task("MentalHealthHistory","Mental Health History and Treatment", Task.TYPE.questions, 2 ));
        pretest.addTask(new Task("AnxietyIdentity","Anxiety and Me", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        pretest.addTask(new Task("AnxietyTriggers","Anxiety Triggers", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.jspsych, 5));
        pretest.addTask(new Task("RR","Completing Short Stories - Continued", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("BBSIQ","Why Things Happen", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("Comorbid","Mood and Drinking Patterns", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("Wellness","What I Believe", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("Mechanisms","How I Respond", Task.TYPE.questions, 0 ));
        sessions.add(pretest);

        session1 = new Session(FIRST_SESSION, "Level 1: Beginner", 0, 0);
        session1.setIndex(1);
        session1.addTask(new Task("Affect","Pre-Affect", "pre", Task.TYPE.questions, 0));
        session1.addTask(new Task("1", "Training Stories", Task.TYPE.angular, 20));
        session1.addTask(new Task("Affect","Post-Affect", "post", Task.TYPE.questions, 0));
        session1.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session1.addTask(new Task("CoachPrompt","Feedback", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("ReturnIntention","Intention to Return", Task.TYPE.questions, 0));
        sessions.add(session1);

        session2 = new Session(SECOND_SESSION, "Level 2: Intermediate", 0, 7);
        session2.setIndex(2);
        session2.addTask(new Task("2", "Training Stories", Task.TYPE.angular, 20));
        session2.addTask(new Task("OA","Anxiety review", Task.TYPE.questions, 1 ));
        session2.addTask(new Task("ReturnIntention","Intention to Return", Task.TYPE.questions, 0));
        sessions.add(session2);

        session3 = new Session(THIRD_SESSION, "Level 3: Advanced", 500, 7);
        session3.setIndex(3);
        session3.addTask(new Task("Affect","Pre-Affect", "pre", Task.TYPE.questions, 0));
        session3.addTask(new Task("3", "Training Stories", Task.TYPE.angular, 20));
        session3.addTask(new Task("Affect","Post-Affect", "post", Task.TYPE.questions, 0));
        session3.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("AnxietyIdentity","Anxiety and Me", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session3.addTask(new Task("DASS21_AS","Mood Assessment", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.jspsych, 5));
        session3.addTask(new Task("RR","Completing Short Stories - Continued", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("BBSIQ","Why Things Happen", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("Comorbid","Mood and Drinking Patterns", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("Wellness","What I Believe", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("Mechanisms","How I Respond", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("ReturnIntention","Intention to Return", Task.TYPE.questions, 0));

        sessions.add(session3);

        session4 = new Session(FOURTH_SESSION, "Level 4: Expert", 0, 7);
        session4.setIndex(4);
        session4.addTask(new Task("4", "Training Stories", Task.TYPE.angular, 20));
        session4.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session4.addTask(new Task("ReturnIntention","Intention to Return", Task.TYPE.questions, 0));
        sessions.add(session4);

        session5 = new Session(FIFTH_SESSION, "Level 5: Master", 500, 7);
        session5.setIndex(5);
        session5.addTask(new Task("Affect","Pre-Affect", "pre", Task.TYPE.questions, 0));
        session5.addTask(new Task("5", "Training Stories", Task.TYPE.angular, 20));
        session5.addTask(new Task("Affect","Post-Affect", "post", Task.TYPE.questions, 0));
        session5.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("AnxietyIdentity","Anxiety and Me", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session5.addTask(new Task("DASS21_AS","Mood Assessment", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.jspsych, 5));
        session5.addTask(new Task("RR","Completing Short Stories - Continued", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("BBSIQ","Why Things Happen", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("Comorbid","Mood and Drinking Patterns", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("Wellness","What I Believe", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("Mechanisms","How I Respond", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("HelpSeeking","Change in Help-Seeking Behavior", Task.TYPE.questions, 1));
        session5.addTask(new Task("Evaluation","Evaluating the Program", Task.TYPE.questions, 2));
        session5.addTask(new Task("AssessingProgram","Assessing the Program", Task.TYPE.questions, 2));

        sessions.add(session5);

        post = new Session(POST_FOLLOWUP, "2 Month Follow-up", 1000, 60);
        post.setIndex(6);
        post.addTask(new Task("AnxietyIdentity","Anxiety and Me", Task.TYPE.questions, 0 ));
        post.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        post.addTask(new Task("DASS21_AS","Mood Assessment", Task.TYPE.questions, 0 ));
        post.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.jspsych, 5));
        post.addTask(new Task("RR","Completing Short Stories - Continued", Task.TYPE.questions, 0 ));
        post.addTask(new Task("BBSIQ","Why Things Happen", Task.TYPE.questions, 0 ));
        post.addTask(new Task("Comorbid","Mood and Drinking Patterns", Task.TYPE.questions, 0 ));
        post.addTask(new Task("Wellness","What I Believe", Task.TYPE.questions, 0 ));
        post.addTask(new Task("Mechanisms","How I Respond", Task.TYPE.questions, 0 ));
        post.addTask(new Task("HelpSeeking","Change in Help-Seeking Behavior", Task.TYPE.questions, 1));
        sessions.add(post);

        return sessions;
    }
}
