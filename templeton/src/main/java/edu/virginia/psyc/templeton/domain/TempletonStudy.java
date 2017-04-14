package edu.virginia.psyc.templeton.domain;

import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
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
@DiscriminatorValue("Templeton")
public class TempletonStudy extends BaseStudy {

    // positive (all positive words)
    // positive (all postivie words, w/ negation statement
    // 50/50 (half positive, half negative, completely random)
    // 50/50 (half positive, half negative, chunks - first 5 pos, next x are negative ....)
    // Neutral condition (likely alternate content)
    public enum CONDITION {POSITIVE, POSITIVE_NEGATION, FIFTY_FIFTY_RANDOM, FIFTY_FIFTY_BLOCKED, NEUTRAL }

    private CONDITION     conditioning;
    public static final String PRE_TEST = "preTest";

    public enum SESSION {firstSession, secondSession, thirdSession, fourthSession, PostFollowUp }

    public static final String FIRST_SESSION = "firstSession";
    public static final String SECOND_SESSION = "secondSession";
    public static final String THIRD_SESSION = "thirdSession";
    public static final String FOURTH_SESSION = "fourthSession";
    public static final String POST_FOLLOWUP = "PostFollowUp";

    @Override
    public String getName() {return "Templeton";}

    public TempletonStudy() {
        this.currentSession = PRE_TEST;
    }

    public TempletonStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    public void completeEligibility(ExpectancyBias bias) {
        TaskLog t = new TaskLog();
        t.setDateCompleted(bias.getDate());
        t.setTaskName("ExpectancyBias");
        t.setSessionName("Eligibility");
        t.setStudy(this);
        taskLogs.add(t);
    }

    @Override
    /**
      Settings for Templeton are based on the session.
       session 1:  use first word fragment ending, followed by Comprehension Question [y/n]
       session 2:  use second word fragment ending, followed by Comprehension Question [mc1]:
       session 3:  use first word fragment ending, followed by second Comprehension Question [mc2]
       session 4:  use second word fragment ending, followed by Comprehension Question [y/n]:
     */
    public Map<String,Object> getPiPlayerParameters() {
        Map<String,Object> map = super.getPiPlayerParameters();
        String sessionName = this.getCurrentSession().getName();
        map.put("negate",(conditioning.equals(CONDITION.POSITIVE_NEGATION)));

        switch(sessionName) {
            case FIRST_SESSION:
                map.put("secondWordSet",false);
                map.put("question","yes_no");
                map.put("lettersToRemove",1);
                break;
            case SECOND_SESSION:
                map.put("secondWordSet",true);
                if(conditioning.equals(CONDITION.NEUTRAL))
                    map.put("question","yes_no");
                else
                    map.put("question", "mc1");
                map.put("lettersToRemove",1);
                break;
            case THIRD_SESSION:
                map.put("secondWordSet",false);
                if(conditioning.equals(CONDITION.NEUTRAL))
                    map.put("question","yes_no");
                else
                    map.put("question", "mc2");
                map.put("lettersToRemove",2);
                break;
            case FOURTH_SESSION:
                map.put("secondWordSet",true);
                map.put("question","yes_no");
                map.put("lettersToRemove",2);
                break;
            default:  // This should only occur in testing / accessing admin etc...
                map.put("secondWordSet",true);
                map.put("question","yes_no");
                map.put("lettersToRemove",1);
        }
        map.put("condition", this.conditioning.toString());
        return map;
    }

    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session pretest, session1, session2, session3, session4, post;

        pretest = new Session (PRE_TEST, "Initial Assessment", 0, 0);
        pretest.setIndex(0);
        pretest.addTask(new Task("Demographics","Personal Background", Task.TYPE.questions, 2));
        pretest.addTask(new Task("MentalHealthHistory","Mental Health History", Task.TYPE.questions, 2));
        pretest.addTask(new Task("WhatIBelieve","What I Believe", Task.TYPE.questions, 2));
        pretest.addTask(new Task("Phq4","My Mood", Task.TYPE.questions, 0));
        sessions.add(pretest);

        session1 = new Session(FIRST_SESSION, "Level 1: Beginner", 0, 0);
        session1.setIndex(1);
        session1.addTask(new Task("Affect","Current Feelings", "pre", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session1.addTask(new Task("Affect","Current Feelings", "post", Task.TYPE.questions, 0));
        session1.addTask(new Task("Relatability","Relatability Follow Up", Task.TYPE.questions, 0));
        session1.addTask(new Task("ExpectancyBias","What Happens Next", Task.TYPE.questions, 2));
        sessions.add(session1);

        session2 = new Session(SECOND_SESSION, "Level 2: Intermediate", 0, 2);
        session2.setIndex(2);
        session2.addTask(new Task("Affect","Current Feelings", "pre", Task.TYPE.questions, 0));
        session2.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session2.addTask(new Task("Affect","Current Feelings", "post", Task.TYPE.questions, 0));
        session2.addTask(new Task("ExpectancyBias","What Happens Next", Task.TYPE.questions, 2));
        session2.addTask(new Task("WhatIBelieve","What I Believe", Task.TYPE.questions, 2));
        session2.addTask(new Task("Phq4","My Mood", Task.TYPE.questions, 0));
        sessions.add(session2);

        session3 = new Session(THIRD_SESSION, "Level 3: Advanced", 0, 2);
        session3.setIndex(3);
        session3.addTask(new Task("Affect","Current Feelings", "pre", Task.TYPE.questions, 0));
        session3.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session3.addTask(new Task("Affect","Current Feelings", "post", Task.TYPE.questions, 0));
        session3.addTask(new Task("ExpectancyBias","What Happens Next", Task.TYPE.questions, 2));
        sessions.add(session3);

        session4 = new Session(FOURTH_SESSION, "Level 4: Expert", 0, 2);
        session4.setIndex(4);
        session4.addTask(new Task("Affect","Current Feelings", "pre", Task.TYPE.questions, 0));
        session4.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session4.addTask(new Task("Affect","Current Feelings", "post", Task.TYPE.questions, 0));
        session4.addTask(new Task("Relatability","Relatability Follow Up", Task.TYPE.questions, 0));
        session4.addTask(new Task("ExpectancyBias","What Happens Next", Task.TYPE.questions, 2));
        session4.addTask(new Task("WhatIBelieve","What I Believe", Task.TYPE.questions, 2));
        session4.addTask(new Task("Phq4","Mood", Task.TYPE.questions, 0));
        session4.addTask(new Task("HelpSeeking","Change in Help Seeking", Task.TYPE.questions, 1));
        sessions.add(session4);

        post = new Session(POST_FOLLOWUP, "1 Month Post Training", 0, 30);
        post.setIndex(5);
        post.addTask(new Task("ExpectancyBias","What Happens Next", Task.TYPE.questions, 2));
        post.addTask(new Task("WhatIBelieve","What I Believe", Task.TYPE.questions, 2));
        post.addTask(new Task("Phq4","Mood", Task.TYPE.questions, 0));
        post.addTask(new Task("HelpSeeking","Change in Help Seeking", Task.TYPE.questions, 1));
        post.addTask(new Task("Evaluation","Evaluating the Program", Task.TYPE.questions, 2));
        sessions.add(post);

        return sessions;
    }
}
