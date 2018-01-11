package edu.virginia.psyc.r01.domain;

import edu.virginia.psyc.r01.persistence.ExpectancyBias;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This is where you define the sessions and tasks that make up the study.
 */
@Entity
@Data
@DiscriminatorValue("R01")
public class R01Study extends BaseStudy {

    // positive (all positive words)
    // positive (all postivie words, w/ negation statement
    // 50/50 (half positive, half negative, completely random)
    // 50/50 (half positive, half negative, chunks - first 5 pos, next x are negative ....)
    // Neutral condition (likely alternate content)
    public enum CONDITION {POSITIVE, POSITIVE_NEGATION, FIFTY_FIFTY_RANDOM, FIFTY_FIFTY_BLOCKED, NEUTRAL }
    //private boolean        increase30 = false;
    private boolean        increase50 = false;
    public static final String PRE_TEST = "preTest";

    public enum SESSION {preTest, firstSession, secondSession, thirdSession, fourthSession, fifthSession, PostFollowUp };

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

    public void completeEligibility(ExpectancyBias bias) {
        TaskLog t = new TaskLog();
        t.setDateCompleted(bias.getDate());
        t.setTaskName("ExpectancyBias");
        t.setSessionName("Eligibility");
        t.setStudy(this);
        taskLogs.add(t);
    }

    @Override
    /** Check out the Templeton Study for building a more complex setup. */
    public Map<String,Object> getPiPlayerParameters() {
        Map<String,Object> map = super.getPiPlayerParameters();
        String sessionName = this.getCurrentSessionModel().getName();
        map.put("negate",(conditioning.equals(CONDITION.POSITIVE_NEGATION)));
        map.put("sessionIndex", this.getCurrentSessionModel().getIndex());
        map.put("secondWordSet",false);
        map.put("question","yes_no");
        map.put("lettersToRemove",1);
        map.put("conditioning", this.conditioning.toString());
        return map;
    }

    public List<String>getConditions(){
        return Stream.of(CONDITION.values()) .map(Enum::name) .collect(Collectors.toList());
    }

    public boolean inSession() {
        return (!getCurrentSessionModel().getName().equals(R01Study.SESSION.preTest.toString()) &&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.firstSession.toString()) &&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.secondSession.toString()) &&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.thirdSession.toString())&&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.fourthSession.toString())&&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.fifthSession.toString())&&
                !getCurrentSessionModel().getName().equals(R01Study.SESSION.PostFollowUp.toString()));

    }


    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {
        List<Session> sessions = new ArrayList<>();
        Session pretest, session1, session2, session3, session4, session5, post;

        pretest = new Session (PRE_TEST, "Initial Assessment", 0, 0);
        pretest.setIndex(0);
        pretest.addTask(new Task("Credibility"," Readiness Rulers and Credibility", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("Demographics","Demographics", Task.TYPE.questions, 2 ));
        pretest.addTask(new Task("MentalHealthHistory","Mental Health History", Task.TYPE.questions, 2 ));
        pretest.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        pretest.addTask(new Task("RR","Recognition Ratings for Measuring Change in Interpretation Bias ", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("BBSIQ","Brief Body Sensations Interpretations Questionnaire", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("DASS21_DS","Depression, Anxiety, Stress Scales-Short Form: Depression Subscale ", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("DD","Daily Drinking Questionnaire", Task.TYPE.questions, 15 ));
        pretest.addTask(new Task("QOL","Quality of Life Scale", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("NGSES","New General Self Efficacy Scale", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("PBS","Growth Mindset Measure", Task.TYPE.questions, 0 ));
        pretest.addTask(new Task("AnxietyTriggers","Additional Items for Judging Caseness & Anxiety Triggers", Task.TYPE.questions, 0 ));


        sessions.add(pretest);

        session1 = new Session(FIRST_SESSION, "Level 1: Beginner", 0, 0);
        session1.setIndex(1);
        session1.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "pre", Task.TYPE.questions, 0));
        session1.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session1.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "post", Task.TYPE.questions, 0));
        session1.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session1.addTask(new Task("ReasonsForEnding","Reasons For Ending", Task.TYPE.questions, 0 ));
        sessions.add(session1);

        session2 = new Session(SECOND_SESSION, "Level 2: Intermediate", 0, 2);
        session2.setIndex(2);
        session2.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session2.addTask(new Task("OA","Overall Anxiety Severity and Impairment Scale", Task.TYPE.questions, 1 ));
        sessions.add(session2);

        session3 = new Session(THIRD_SESSION, "Level 3: Advanced", 0, 2);
        session3.setIndex(3);
        session3.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "pre", Task.TYPE.questions, 0));
        session3.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session3.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "post", Task.TYPE.questions, 0));
        session3.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session3.addTask(new Task("RR","Recognition Ratings for Measuring Change in Interpretation Bias ", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("BBSIQ","Brief Body Sensations Interpretations Questionnaire", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("DASS21_DS","Depression, Anxiety, Stress Scales-Short Form: Depression Subscale ", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("DD","Daily Drinking Questionnaire", Task.TYPE.questions, 15 ));
        session3.addTask(new Task("QOL","Quality of Life Scale", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("NGSES","New General Self Efficacy Scale", Task.TYPE.questions, 0 ));
        session3.addTask(new Task("PBS","Growth Mindset Measure", Task.TYPE.questions, 0 ));
        sessions.add(session3);

        session4 = new Session(FOURTH_SESSION, "Level 4: Expert", 0, 2);
        session4.setIndex(4);
        session4.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session4.addTask(new Task("OA","OA", Task.TYPE.questions, 1 ));
        sessions.add(session4);

        session5 = new Session(FIFTH_SESSION, "Level 5: Master", 0, 2);
        session5.setIndex(5);
        session5.addTask(new Task("MSUE","MSUE", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "pre", Task.TYPE.questions, 0));
        session5.addTask(new Task("JsPsychTrial", "Training Stories", Task.TYPE.jspsych, 20));
        session5.addTask(new Task("Affect","Pre-training Subjective Units of Distress Scale", "post", Task.TYPE.questions, 0));
        session5.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session5.addTask(new Task("RR","Recognition Ratings for Measuring Change in Interpretation Bias ", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("BBSIQ","Brief Body Sensations Interpretations Questionnaire", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("DASS21_DS","Depression, Anxiety, Stress Scales-Short Form: Depression Subscale ", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("DD","Daily Drinking Questionnaire", Task.TYPE.questions, 15 ));
        session5.addTask(new Task("QOL","Quality of Life Scale", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("NGSES","New General Self Efficacy Scale", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("PBS","Growth Mindset Measure", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("DASS21_AS","Depression, Anxiety, Stress Scales-Short Form: Anxiety Subscale", Task.TYPE.questions, 0 ));
        session5.addTask(new Task("HelpSeeking","Help Seeking", Task.TYPE.questions, 1 ));
        sessions.add(session5);

        post = new Session(POST_FOLLOWUP, "1 Month Post Training", 0, 30);
        post.setIndex(6);
        post.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        post.addTask(new Task("RR","Recognition Ratings for Measuring Change in Interpretation Bias ", Task.TYPE.questions, 0 ));
        post.addTask(new Task("BBSIQ","Brief Body Sensations Interpretations Questionnaire", Task.TYPE.questions, 0 ));
        post.addTask(new Task("DASS21_DS","Depression, Anxiety, Stress Scales-Short Form: Depression Subscale ", Task.TYPE.questions, 0 ));
        post.addTask(new Task("DD","Daily Drinking Questionnaire", Task.TYPE.questions, 15 ));
        post.addTask(new Task("QOL","Quality of Life Scale", Task.TYPE.questions, 0 ));
        post.addTask(new Task("NGSES","New General Self Efficacy Scale", Task.TYPE.questions, 0 ));
        post.addTask(new Task("PBS","Growth Mindset Measure", Task.TYPE.questions, 0 ));
        post.addTask(new Task("DASS21_AS","Depression, Anxiety, Stress Scales-Short Form: Anxiety Subscale", Task.TYPE.questions, 0 ));
        post.addTask(new Task("HelpSeeking","Help Seeking", Task.TYPE.questions, 1 ));
        sessions.add(post);

        return sessions;
    }
}
