package edu.virginia.psyc.mindtrails.controllers;

import edu.virginia.psyc.mindtrails.MockClasses.TestStudy;
import edu.virginia.psyc.mindtrails.domain.Participant;


/**
 * Just some common tools for executing tests.
 */
public class BaseControllerTest {

    Participant getUser() {
        Participant p = new Participant("John", "js@st.com", false);
        p.setStudy(new TestStudy());
        p.getStudy().forceToSession("PRE");
        return p;
    }

    Participant getAdmin() {
        Participant p = new Participant("JohnAdmin", "js@st.com", true);
        p.setStudy(new TestStudy());
        p.getStudy().forceToSession("PRE");
        return p;
    }
}
