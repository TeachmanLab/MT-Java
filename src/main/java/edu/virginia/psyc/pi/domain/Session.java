package edu.virginia.psyc.pi.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/12/14
 * Time: 8:21 AM
 * This session class is used to return an array of sessions designed for easy display
 * in the user interface.
 */
public class Session {


    /** This is an ordered enumeration, and is used that way.
     *  Changing the order here will impact the order in which
     *  sessions occur in the interface and in their progression.
     */
    public enum NAME {ELIGIBLE, PRE, SESSION1, SESSION2, SESSION3, SESSION4, SESSION5, SESSION6, SESSION7, SESSION8, POST, COMPLETE}
    public enum STATE {COMPLETE,CURRENT,INCOMPLETE}

    private NAME       name;

    private boolean    complete;
    private boolean    current;
    private List<Task> tasks;


    public static List<Session> defaultList() {
        return createListView(NAME.values()[0], 0);
    }

    /**
     * Generates a list of sessions suitable for display, based on the current session, and index of the
     * current task within that Session.
     * @return
     */
    public static List<Session> createListView(NAME currentName, int taskIndex) {
        List<Session> list = new ArrayList<Session>();
        boolean completed = true;
        boolean current   = false;
        Session session;

        for(Session.NAME name : Session.NAME.values()) {
            if(name == currentName) {
                completed = false;
                current = true;
            }
            if(!name.equals(NAME.ELIGIBLE) && !name.equals(NAME.COMPLETE)) {
                session = new Session(name, completed, current);
                session.setTasks(getTasks(name, taskIndex));
                list.add(session);
            }
            current = false;  // only one can be current.
        }
        return list;
    }

    /**
     * In the future we should refactor this, so that we load the sessions from a
     * configuration file, or from the database.  But neither of these directions
     * seems clean to me, as they will naturally depend on certain code and paths
     * existing.  So here is how sessions and tasks are defined, until requirements,
     * or common sense dictate a more complex solution.
     * @param name The Name of a given session.
     * @return
     */
    public static List<Task> getTasks(NAME name, int taskIndex) {

        List<Task> tasks = new ArrayList<Task>();
        switch(name) {
            case PRE:
                tasks.add(new Task("OA", "OASIS", Task.TYPE.questions));
                tasks.add(new Task("credibility", "Credibility Assessment", Task.TYPE.questions));
                tasks.add(new Task("MH", "Mental Health History", Task.TYPE.questions));
                tasks.add(new Task("SA", "State Anxiety", Task.TYPE.questions));
                tasks.add(new Task("RecognitionRatings", "RecogRatings", Task.TYPE.playerScript));
                tasks.add(new Task("RR", "RecognitionRatings", Task.TYPE.questions));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions));
                break;
            case SESSION1:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("FirstSessionComplete", "First Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                break;
            case SESSION2:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("SecondSessionComplete", "Second Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                break;
            case SESSION3:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("ThirdSessionComplete", "Third Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions));
                break;
            case SESSION4:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("FourthSessionComplete", "Fourth Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions));
                break;
            case SESSION5:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("FirstSessionComplete", "First Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                break;
            case SESSION6:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("SecondSessionComplete", "Second Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions));
                break;
            case SESSION7:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("ThirdSessionComplete", "Third Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                break;
            case SESSION8:
                tasks.add(new Task("AIP", "Imagery Prime", Task.TYPE.questions));
                tasks.add(new Task("FourthSessionComplete", "Fourth Session", Task.TYPE.playerScript));
                tasks.add(new Task("CC", "CompareContrast", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions));
                break;
            case POST:
                tasks.add(new Task("MUE", "User Experience", Task.TYPE.questions));
                tasks.add(new Task("DASS21_AS", "Status Questionnaire", Task.TYPE.questions));
                tasks.add(new Task("SAPo", "State Anxiety", Task.TYPE.questions));
                tasks.add(new Task("QOL", "Quality of Life Scale", Task.TYPE.questions));
                tasks.add(new Task("DASS21_DS", "Symptom Measures", Task.TYPE.questions));
        }
        setTaskStates(tasks, taskIndex);
        return tasks;
    }

    /**
     * This method churns through the list of tasks, setting the "current" and "complete" flags based on the
     * current task index.
     */
    private static void setTaskStates(List<Task> tasks, int taskIndex) {
        int index = 0;
        for(Task t : tasks) {
            t.setCurrent(taskIndex == index);
            t.setComplete(taskIndex > index);
            index ++;
        }

    }

    /**
     * Given a session name, returns the next session name.
     */
    public static NAME nextSession(NAME last) {
        boolean nextOne = false;

        for(Session.NAME name : Session.NAME.values()) {
            if(nextOne) return name;
            if(name == last) nextOne = true;
        }
        return null;
    }

    /** State is one of the STATE enumerations defined at the head of this class.
     * @return Current State of this Session (incomplete / current / complete).
     */
    public STATE getState() {
        if(current) return STATE.CURRENT;
        else if(complete) return STATE.COMPLETE;
        else return STATE.INCOMPLETE;
    }

    public Task getCurrentTask() {
        for(Task t : tasks) {
            if (t.isCurrent()) return t;
        }
        return null;
    }

    /**
     * Returns the index of the current task in the list of tasks in this session.
     * This is a 0-based index.
     * @return
     */
    public int getCurrentTaskIndex() {
        int counter = 0;
        for(Task t : tasks) {
            if (t.isCurrent()) return counter;
            counter ++;
        }
        return 0;
    }

    public Session() {}

    public Session(NAME name, boolean complete, boolean current) {
        this.name = name;
        this.complete = complete;
        this.current = current;
    }


    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public NAME getName() {
        return name;
    }

    public void setName(NAME name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
