package edu.virginia.psyc.spanish.domain;

import lombok.Data;
import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.Session;
import org.mindtrails.domain.Task;
import org.mindtrails.domain.questionnaire.QuestionnaireData;
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
@DiscriminatorValue("SPANISH")
public class SpanishStudy extends BaseStudy {

    public enum CONDITION {ENGLISH_BILINGUAL, SPANISH_DOMINANT, SPANISH_BILINGUAL}
    public static Map<String, CONDITION> conditionMappings = new HashMap<String, CONDITION>();

    public enum SESSION { firstSession, secondSession }

    public enum STUDY_EXTENSIONS {}

    public static final String FIRST_SESSION = "firstSession";
    public static final String SECOND_SESSION = "secondSession";

    @Override
    public String getName() {return "SPANISH";}

    public SpanishStudy() {
        this.currentSession = FIRST_SESSION;

        conditionMappings.put("enb", CONDITION.ENGLISH_BILINGUAL);
        conditionMappings.put("esd", CONDITION.SPANISH_DOMINANT);
        conditionMappings.put("esb", CONDITION.SPANISH_BILINGUAL);
    }

    public SpanishStudy(String currentSession, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs, boolean receiveGiftCards) {
        super(currentSession, taskIndex, lastSessionDate, taskLogs, receiveGiftCards);
    }

    public void completeEligibility(QuestionnaireData q) {
        TaskLog t = new TaskLog();
        t.setDateCompleted(q.getDate());
        t.setTaskName(q.getClass().getSimpleName());
        t.setSessionName("Eligibility");
        t.setStudy(this);
        t.setTimeOnTask(q.getTimeOnPage());
        taskLogs.add(t);
    }

    @Override
    /** Check out the Templeton Study for building a more complex setup. */
    public Map<String,Object> getPiPlayerParameters() {
        // No longer used, just return an empty map.
        Map<String,Object> map = super.getPiPlayerParameters();
        return map;
    }

    public List<String>getConditions(){
        return Stream.of(CONDITION.values()) .map(Enum::name) .collect(Collectors.toList());
    }

    public boolean inSession() {
        return (!getCurrentSession().getName().equals(SpanishStudy.SESSION.firstSession.toString()) &&
                !getCurrentSession().getName().equals(SpanishStudy.SESSION.secondSession.toString()));
    }


    /**
     * Returns the list of sessions and tasks that define the study.
     * @return
     */
    @Override
    public List<Session> getStatelessSessions() {

        List<Session> sessions = new ArrayList<>();
        Session session1, session2;

        session1 = new Session(FIRST_SESSION, "level1", 0, 0);
        session1.setIndex(1);

        session1.addTask(new Task("Demographics","Personal Background", Task.TYPE.questions, 2 ));
        session1.addTask(new Task("MentalHealthHistory","Mental Health and Treatment History", Task.TYPE.questions, 2 ));
        session1.addTask(new Task("Acculturation","Language Preferences", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("Ethnicity","My Ethnicity", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("Comorbid","Mood Assessment", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.angular, 5));
        session1.addTask(new Task("RR","Completing Short Stories, Pt. 2", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("Covid19","COVID-19", Task.TYPE.questions, 0 ));
        session1.addTask(new Task("Affect","Current Feelings, Pre", "pre", Task.TYPE.questions, 0));
        session1.addTask(new Task("1", "Training Session 1", Task.TYPE.angular, 20));
        session1.addTask(new Task("Affect","Current Feelings, Post", "post", Task.TYPE.questions, 0));
        session1.addTask(new Task("CC","Compare and Contrast", Task.TYPE.questions, 0 ));
        sessions.add(session1);


        session2 = new Session(SECOND_SESSION, "level2", 0, 5);
        session2.setIndex(2);
        session2.addTask(new Task("Affect","Current Feelings, Pre", "pre", Task.TYPE.questions, 0));
        session2.addTask(new Task("2", "Training Session 2", Task.TYPE.angular, 20));
        session2.addTask(new Task("Affect","Current Feelings, Post", "post", Task.TYPE.questions, 0));
        session2.addTask(new Task("OA","Anxiety Review", Task.TYPE.questions, 1 ));
        session2.addTask(new Task("Comorbid","Mood Assessment", Task.TYPE.questions, 0 ));
        session2.addTask(new Task("DASS21_AS","Mood Assessment, Pt. 2", Task.TYPE.questions, 0 ));
        session2.addTask(new Task("recognitionRatings", "Completing Short Stories", Task.TYPE.angular, 5));
        session2.addTask(new Task("RR","Completing Short Stories, Pt. 2", Task.TYPE.questions, 0 ));
        session2.addTask(new Task("Evaluation","Evaluating the Program", Task.TYPE.questions, 2));
        sessions.add(session2);


        return sessions;
    }
}