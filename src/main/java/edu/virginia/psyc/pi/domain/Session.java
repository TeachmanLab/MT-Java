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
    public enum NAME {ELIGIBLE, PRE, WEEK1A, WEEK1B, WEEK2A, WEEK2B, WEEK3A, WEEK3B, WEEK4A, WEEK4B, POST, COMPLETE}

    public enum PRE {DASS21_AS, Credibility}

    private NAME    name;
    private boolean complete;
    private boolean current;
    private String  group;




    /**
     * Generates a list of sessions suitable for display, based on the current session.
     * @return
     */
    public static List<Session> createListView(NAME currentName) {
        List<Session> list = new ArrayList<Session>();
        boolean completed = true;
        boolean current   = false;
        String group = "";

        for(Session.NAME s : Session.NAME.values()) {
            if(s == currentName) {
                completed = false;
                current = true;
            }
            if (s.name().matches("PRE|POST")) group = s.name().toLowerCase();
            else group = s.name().substring(0,5).toLowerCase();
            if(!s.equals(NAME.ELIGIBLE) && !s.equals(NAME.COMPLETE))
                list.add(new Session(s, completed, current, group));
            current = false;  // only one can be current.
        }
        return list;
    }

    public Session() {}

    public Session(NAME name, boolean complete, boolean current, String group) {
        this.name = name;
        this.complete = complete;
        this.current = current;
        this.group   = group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
