package edu.virginia.psyc.mindtrails.domain.forms;

import edu.virginia.psyc.mindtrails.domain.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 9/2/14
 * Time: 3:50 PM
 * Just your basic everyday list of Participants.
 * (This helps us edit a whole set of participants all at once)
 */
public class ParticipantListForm {

    private List<Participant> participants;

    private List<String> sessionNames;

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void add(Participant p) {
        if(null == this.participants) this.participants = new ArrayList<Participant>();
        this.participants.add(p);
    }

    public List<String> getSessionNames() {
        return sessionNames;
    }

    public void setSessionNames(List<String> sessionNames) {
        this.sessionNames = sessionNames;
    }

    public void add(String name) {
        if(null == this.sessionNames) this.sessionNames= new ArrayList<String>();
        this.sessionNames.add(name);
    }


}
