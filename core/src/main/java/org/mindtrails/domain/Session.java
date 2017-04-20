package org.mindtrails.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/12/14
 * Time: 8:21 AM
 * This session class is used to return an array of sessions designed for easy display
 * in the user interface.
 */
@Data
public class Session {


    public enum STATE {COMPLETE, CURRENT, INCOMPLETE}
    private static final Task nullTask = new Task("null", "no task", Task.TYPE.questions, 0, false, false, new Date());

    private int index; // Used to reference image links in the r34 study, need to clean this up or remove it at some point.
    private String name;
    private String displayName;
    private boolean complete = false;
    private boolean current = false;
    private int giftAmount;
    private int daysToWait;  // Number of days to wait before user can start session.
    @JsonIgnore
    private List<Task> tasks = new ArrayList<Task>();

    public Session() {
    }

    public Session(String name, String displayName, int giftAmount, int daysToWait) {
        this.name = name;
        this.displayName = displayName;
        this.giftAmount = giftAmount;
        this.daysToWait = daysToWait;
    }

    public Session(String name, String displayName, int giftAmount, int daysToWait, List<Task> tasks) {
        this.name = name;
        this.displayName = displayName;
        this.giftAmount = giftAmount;
        this.daysToWait = daysToWait;
        this.tasks = tasks;
    }

    public void addTask(Task t) {
        this.tasks.add(t);
    }

    /**
     * State is one of the STATE enumerations defined at the head of this class.
     *
     * @return Current State of this Session (incomplete / current / complete).
     */
    public STATE getState() {
        if (current) return STATE.CURRENT;
        else if (complete) return STATE.COMPLETE;
        else return STATE.INCOMPLETE;
    }

    public Task getCurrentTask() {
        for (Task t : tasks) {
            if (t.isCurrent()) return t;
        }
        return nullTask;
    }

    /**
     * Returns the index of the current task in the list of tasks in this session.
     * This is a 0-based index.
     *
     * @return
     */
    public int getCurrentTaskIndex() {
        int counter = 0;
        for (Task t : tasks) {
            if (t.isCurrent()) return counter;
            counter++;
        }
        return 0;
    }

    /**
     * Returns the total time in minutes it should take to complete the session
     */
    public int getDuration() {
        int total = 0;
        for (Task t : tasks) {
            total += t.getDuration();
        }
        return total;
    }

}

