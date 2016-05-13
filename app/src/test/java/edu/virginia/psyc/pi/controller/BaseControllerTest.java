package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.pi.domain.PiParticipant;


/**
 * Just some common tools for executing tests.
 */
public class BaseControllerTest {

    Participant getUser() {
        Participant p = new PiParticipant("John", "js@st.com", false);
        p.getStudy().forceToSession("PRE");
        return p;
    }

    Participant getAdmin() {
        Participant p = new PiParticipant("JohnAdmin", "js@st.com", true);
        p.getStudy().forceToSession("PRE");
        return p;
    }
}
