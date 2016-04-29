package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Just some common tools for executing tests.
 */
public class BaseControllerTest {

    ParticipantDAO getUser() {
        ParticipantDAO dao = new ParticipantDAO("John", "js@st.com","1234", false, "blue");
        dao.setCurrentSession("PRE");
        return dao;
    }

    ParticipantDAO getAdmin() {
        ParticipantDAO dao = new ParticipantDAO("JohnAdmin", "js@st.com","1234", true, "blue");
        dao.setCurrentSession("PRE");
        return dao;
    }
}
