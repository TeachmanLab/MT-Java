package org.mindtrails.domain;

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

    private int index;
    private String name;
    private String displayName;
    private boolean complete;
    private boolean current;
    private int giftAmount;
    private List<Task> tasks = new ArrayList<Task>();

    public Session() {
    }

    public Session(int index, String name, String displayName, boolean complete, boolean current, int giftAmount, List<Task> tasks) {
        this.index = index;
        this.name = name;
        this.complete = complete;
        this.current = current;
        this.displayName = displayName;
        this.giftAmount = giftAmount;
        this.tasks = tasks;
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



}

