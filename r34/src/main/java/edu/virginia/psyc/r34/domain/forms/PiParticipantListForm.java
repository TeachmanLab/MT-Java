package edu.virginia.psyc.r34.domain.forms;

import edu.virginia.psyc.r34.domain.PiParticipant;

import java.util.ArrayList;
import java.util.List;

/**
 * Just your basic everyday list of Participants.
 * (This helps us edit a whole set of participants all at once)
 */
public class PiParticipantListForm {

    private List<PiParticipant> participants;

    private List<String> sessionNames;

    public List<PiParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<PiParticipant> participants) {
        this.participants = participants;
    }

    public void add(PiParticipant p) {
        if(null == this.participants) this.participants = new ArrayList<PiParticipant>();
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
