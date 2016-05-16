package edu.virginia.psyc.mindtrails.controllers;

import edu.virginia.psyc.mindtrails.Application;
import edu.virginia.psyc.mindtrails.MockClasses.TestQuestionnaire;
import edu.virginia.psyc.mindtrails.MockClasses.TestQuestionnaireRepository;
import edu.virginia.psyc.mindtrails.MockClasses.TestUndeleteable;
import edu.virginia.psyc.mindtrails.MockClasses.TestUndeleteableRepository;
import edu.virginia.psyc.mindtrails.controller.QuestionController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class QuestionControllerTest extends BaseControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private TestQuestionnaireRepository repository;

    @Autowired
    private TestUndeleteableRepository undeleteableRepository;

    @Autowired
    private QuestionController questionController;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .addFilters(this.springSecurityFilterChain)
                .build();

        /*
        // Process mock annotations
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    */

        repository.deleteAll();
    }

    @After
    public void cleanup() {
    //formRepository.deleteAll();
    }

    private TestQuestionnaire getLastQuestionnaire() {
        repository.flush();
        List<TestQuestionnaire> dataList = repository.findAll();
        return dataList.get(dataList.size() - 1);
    }

    @Test
    public void testGetForm() throws Exception {
        MvcResult result = mockMvc.perform(get("/questions/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(getUser())))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testGetFormContainsCustomParameters() throws Exception {
        MvcResult result = mockMvc.perform(get("/questions/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(getUser())))
                .andExpect((status().is2xxSuccessful()))
                .andExpect(model().attribute("test", "pickles"))
                .andReturn();
    }


    @Test
    public void testPostDataForm() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/TestQuestionnaire")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(getUser()))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti"))
                .andExpect((status().is3xxRedirection()));
    }

    @Test
    public void testPostBadData() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/NoSuchForm")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(getUser()))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti"))
                .andExpect((status().is4xxClientError()));
    }

    @Test
    public void testThatPostedDataIsStored() throws Exception {
        testPostDataForm();
        Assert.assertEquals("cheese", getLastQuestionnaire().getValue());
    }

    @Test
    public void testParticipantDateAndSessionArePopulated() throws Exception {
        testPostDataForm();
        assertNotNull(getLastQuestionnaire().getDate());
        assertNotNull(getLastQuestionnaire().getSession());
        assertNotNull(getLastQuestionnaire().getParticipantRSA());
    }

    @Test
    public void testParticipantIdIsPopulatedIfItExists() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/TestUndeleteable")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(getUser()))
                .param("value", "cheese"))
                .andExpect((status().is3xxRedirection()));

        undeleteableRepository.flush();
        List<TestUndeleteable> dataList = undeleteableRepository.findAll();
        TestUndeleteable last = dataList.get(dataList.size() - 1);
        assertNotNull(last.getParticipant());

    }
}
