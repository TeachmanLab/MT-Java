package edu.virginia.psyc.pi.domain;

import lombok.Data;

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
@Data
public class Session {


    public enum STATE {COMPLETE, CURRENT, INCOMPLETE}

    private String name;
    private String displayName;
    private boolean complete;
    private boolean current;
    private boolean awardGift;
    private List<Task> tasks = new ArrayList<Task>();

    public Session() {
    }

    public Session(String name, String displayName, boolean complete, boolean current, boolean gift, List<Task> tasks) {
        this.name = name;
        this.complete = complete;
        this.current = current;
        this.displayName = displayName;
        this.awardGift = gift;
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
        return null;
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

