package edu.virginia.psyc.pi.domain;

import edu.virginia.psyc.pi.persistence.GiftLogDAO;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/12/14
 * Time: 8:21 AM
 * The Participants progress through a series of sessions
 */
public class CBMStudy implements Study {

    private String currentName;
    private int taskIndex;
    private Date lastSessionDate;
    private List<TaskLog> taskLogs;

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CBMStudy.class);

    /**
     * This is an ordered enumeration, and is used that way.
     * Changing the order here will impact the order in which
     * sessions occur in the interface and in their progression.
     */
    public static enum NAME {
        ELIGIBLE, PRE, SESSION1, SESSION2, SESSION3, SESSION4, SESSION5, SESSION6, SESSION7, SESSION8, POST, COMPLETE
    }

    /** This specifies which Sessions should receive a gift after completion.
     */
    private static List<NAME> giftSessions = new ArrayList<NAME>() {
        {
            add(NAME.PRE);
            add(NAME.SESSION3);
            add(NAME.SESSION6);
        }
    };


    public CBMStudy(String currentName, int taskIndex, Date lastSessionDate, List<TaskLog> taskLogs) {
        this.currentName = currentName;
        this.taskIndex = taskIndex;
        this.lastSessionDate = lastSessionDate;
        this.taskLogs = taskLogs;
    }

    public List<Session> getSessions() {
        List<Session> sessions = new ArrayList<Session>();
        boolean completed = true;
        boolean current = false;
        boolean gift;
        Session session;
        NAME  curName = NAME.valueOf(currentName);

        for (NAME name : NAME.values()) {
            if (name == curName) {
                completed = false;
                current = true;
            }
            gift = false;
            if(giftSessions.contains(name)) gift = true;
            if (!name.equals(NAME.ELIGIBLE) && !name.equals(NAME.COMPLETE)) {
                session = new Session(name.toString(), calculateDisplayName(name), completed, current, gift, getTasks(name, taskIndex));
                sessions.add(session);
            }
            current = false;  // only one can be current.
        }
        return sessions;
    }


    /**
     * @return Number of days since the last completed session.
     * Returns 99 if the user never logged in or completed a session.
     */
    public int daysSinceLastSession() {
        DateTime last;
        DateTime now;

        last = new DateTime(lastSessionDate);
        now  = new DateTime();
        return Days.daysBetween(last, now).getDays();
    }


    /**
     * In the future we should refactor this, so that we load the sessions from a
     * configuration file, or from the database.  But neither of these directions
     * seems clean to me, as they will naturally depend on certain code and paths
     * existing.  So here is how sessions and tasks are defined, until requirements,
     * or common sense dictate a more complex solution.
     *
     * Note:  Arguments to Task are:  unique name, display name, type, and
     * approximate time it takes to complete in minutes.
     *
     * @param name The Name of a given session.
     * @return
     */
    private List<Task> getTasks(NAME name, int taskIndex) {

        List<Task> tasks = new ArrayList<Task>();
        switch (name) {
            case PRE:
                tasks.add(new Task("OA", "OASIS", Task.TYPE.questions, 1));
                tasks.add(new Task("credibility", "Credibility Assessment", Task.TYPE.questions, 1));
                tasks.add(new Task("MH", "Mental Health History", Task.TYPE.questions, 1));
                tasks.add(new Task("demographics", "Demographics", Task.TYPE.questions, 2));
                tasks.add(new Task("SA", "Anxiety Assessment", Task.TYPE.questions, 2));
                tasks.add(new Task("RecognitionRatings", "Recognition Training", Task.TYPE.playerScript, 20));
                tasks.add(new Task("RR", "Recognition Ratings", Task.TYPE.questions, 4));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions, 2));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions, 1));
                break;
            case SESSION1:
                tasks.add(new Task("AIP", "Use Your Imagination", Task.TYPE.questions,5));
                tasks.add(new Task("FirstSessionComplete", "First Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "Compare / Contrast", Task.TYPE.questions,1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions,1));
                break;
            case SESSION2:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions,5));
                tasks.add(new Task("SecondSessionComplete", "Second Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "Compare / Contrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                break;
            case SESSION3:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("ThirdSessionComplete", "Third Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "Compare / Contrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION4:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("FourthSessionComplete", "Fourth Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions, 2));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions, 1));
                break;
            case SESSION5:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("FirstSessionComplete", "First Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                break;
            case SESSION6:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("SecondSessionComplete", "Second Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                break;
            case SESSION7:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("ThirdSessionComplete", "Third Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                break;
            case SESSION8:
                tasks.add(new Task("AIP", "Use your Imagination", Task.TYPE.questions, 5));
                tasks.add(new Task("FourthSessionComplete", "Fourth Session", Task.TYPE.playerScript, 20));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions, 1));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions, 2));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions, 1));
                break;
            case POST:
                tasks.add(new Task("MUE", "User Experience", Task.TYPE.questions, 2));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions, 1));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions, 3));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions, 2));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions, 1));
        }
        setTaskStates(name, tasks, taskIndex);
        return tasks;
    }

    private static String calculateDisplayName(NAME name) {
        String displayName = "UNKNOWN";
        switch (name) {
            case ELIGIBLE: displayName="Eligible"; break;
            case PRE: displayName="Initial Assessment"; break;
            case SESSION1: displayName="Day 1 Training"; break;
            case SESSION2: displayName="Day 2 Training"; break;
            case SESSION3: displayName="Day 3 Training"; break;
            case SESSION4: displayName="Day 4 Training"; break;
            case SESSION5: displayName="Day 5 Training"; break;
            case SESSION6: displayName="Day 6 Training"; break;
            case SESSION7: displayName="Day 7 Training"; break;
            case SESSION8: displayName="Day 8 Training"; break;
            case POST: displayName="Post Assessment"; break;
            case COMPLETE: displayName="Complete"; break;
        }
        return displayName;
    }

    /**
     * Given a session name, returns the next session name.
     */
    private static NAME nextSessionName(NAME last) {
        boolean nextOne = false;

        for (NAME name : NAME.values()) {
            if (nextOne) return name;
            if (name == last) nextOne = true;
        }
        return null;
    }

    public Session getLastSession() {
        NAME last = NAME.ELIGIBLE;

        for (NAME name : NAME.values()) {
            if (name == NAME.valueOf(currentName)) break;
            last = name;
        }
        return new Session(last.toString(), calculateDisplayName(last), true, false, giftSessions.contains(last), getTasks(last,0));
    }

    /**
     * This method churns through the list of tasks, setting the "current" and "complete" flags based on the
     * current task index. It also uses the task logs to determine the completion date.
     */
    private void setTaskStates(NAME sessionName, List<Task> tasks, int taskIndex) {
        int index = 0;
        for (Task t : tasks) {
            t.setCurrent(taskIndex == index);
            t.setComplete(taskIndex > index);
            for(TaskLog log : taskLogs) {
                if (log.getSessionName().equals(sessionName.toString()) && log.getTaskName().equals(t.getName())) {
                    t.setDateCompleted(log.getDateCompleted());
                    break;
                }
            }
            index++;
        }
    }

    public Session getCurrentSession() {
        List<Session> sessions = getSessions();
        for(Session s  : getSessions()) {
            if (s.isCurrent()) {
                return s;
            }
        }
        // If there is no current session, return the first session.
        sessions.get(0).setCurrent(true);
        return sessions.get(0);
    }

    /**
     * Returns true if the session is completed, false otherwise.
     * @param session
     * @return
     */
    public boolean completed(String session) {
        Session currentSession = getCurrentSession();
        if(session == currentSession.getName()) return false;
        for(NAME s : NAME.values()) {
            String strName = s.toString();
            if (strName.equals(session)) return true;
            if (strName.equals(currentSession.getName())) return false;
        }
        // You can't really get here, since we have looped through all
        // the possible values of session.
        return false;
    }

    public void completeCurrentTask() {


        // If this is the last task in a session, then we move to the next session.
        if(taskIndex +1 >= getCurrentSession().getTasks().size()) {
            completeSession();
        } else { // otherwise we just increment the task index.
            this.taskIndex = taskIndex + 1;
        }
    }


    @Override
    public int getCurrentTaskIndex() {
        return taskIndex;
    }

    @Override
    public Date getLastSessionDate() {
        return lastSessionDate;
    }

    @Override
    public void setLastSessionDate(Date date) {
        this.lastSessionDate = date;
    }

    @Override
    public STUDY_STATE getState() {

            if (taskIndex != 0) return STUDY_STATE.IN_PROGRESS;

            // Pre Assessment and Session 1 can be completed immediately.
            if(getCurrentSession().getName().equals(NAME.PRE.toString()) ||
                    getCurrentSession().getName().equals(NAME.SESSION1.toString()))
                return STUDY_STATE.READY;

            // Otherwise, you must wait at least one day before starting the next
            // session.
            if(daysSinceLastSession() == 0 && lastSessionDate != null) return STUDY_STATE.WAIT_A_DAY;
            return STUDY_STATE.READY;
        }

    void completeSession() {
        List<Session> sessions = getSessions();
        boolean next = false;

        Session nextSession = getCurrentSession();
        for(Session s: sessions) {
            if(next == true) { nextSession = s; break; }
            if(s.isCurrent()) next = true;
        }

        this.taskIndex = 0;
        this.currentName = nextSession.getName();
        this.lastSessionDate = new Date();
    }
}
