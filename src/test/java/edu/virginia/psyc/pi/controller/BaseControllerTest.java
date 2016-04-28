package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.DAO.TestQuestionnaire;
import edu.virginia.psyc.pi.DAO.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
