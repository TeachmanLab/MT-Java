package edu.virginia.psyc.pi.domain;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/24/14
 * Time: 4:57 PM
 * A Session is made of an ordered series of Tasks.  Tasks can be either questionniares or
 * PIPlayer scenerios.  A participant completes a session by completing all of it's tasks.
 */
public class Task {

    public enum TYPE {questions, playerScript};
    public enum STATE {COMPLETE,CURRENT,INCOMPLETE}

    private String     name;
    private String     displayName;
    private TYPE       type;
    private boolean    complete;
    private boolean    current;


    public Task(String name, String displayName, TYPE type) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.complete = false;
        this.current = false;
    }


    public Task(String name, String displayName, TYPE type, boolean complete, boolean current) {
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.complete = complete;
        this.current = current;
    }

    /**
     * The request mapping, is the relative url endpoint where the given task
     * resides.  It is a composite of the type and the name.  So a type of "question"
     * and a name of "dass21" would mean a request mapping of "question/dass21" suitable
     * for use when setting a relative path.  This would be expanded out at request
     * time to be something along these lines of "https://projectimplicit.com/question/dass21"
     * @return
     */
    public String getRequestMapping() {
        return "/" + type + "/" + name;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public STATE getState() {
        if(current) return STATE.CURRENT;
        else if(complete) return STATE.COMPLETE;
        else return STATE.INCOMPLETE;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", complete=" + complete +
                ", current=" + current +
                '}';
    }
}
