package edu.virginia.psyc.r34.controller;

import org.mindtrails.domain.Participant;
import edu.virginia.psyc.r34.domain.CBMStudy;


/**
 * Just some common tools for executing tests.
 */
public class BaseControllerTest {

    Participant getUser() {
        Participant p = new Participant("John", "js@st.com", false);
        p.setStudy(new CBMStudy());
        p.getStudy().forceToSession("PRE");
        return p;
    }

    Participant getAdmin() {
        Participant p = new Participant("JohnAdmin", "js@st.com", true);
        p.setStudy(new CBMStudy());
        p.getStudy().forceToSession("PRE");
        return p;
    }
}
