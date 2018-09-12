package org.mindtrails.controllers;

import org.mindtrails.Application;
import org.mindtrails.MockClasses.*;
import org.mindtrails.controller.QuestionController;
import org.mindtrails.domain.Participant;
import org.mindtrails.service.ImportService;
import org.mindtrails.service.ParticipantService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
public class QuestionControllerTest extends BaseControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private TestQuestionnaireRepository repository;

    @Autowired
    private TestUndeleteableRepository undeleteableRepository;

    @Autowired
    private QuestionController questionController;

    @Autowired
    private ImportService importService;

    @Override
    public Object[] getControllers() {
        return(new Object[]{questionController});
    }

    private TestQuestionnaire getLastQuestionnaire() {
        repository.flush();
        List<TestQuestionnaire> dataList = repository.findAll();
        return dataList.get(dataList.size() - 1);
    }

    @Before
    public void setModeToExport() {
        this.importService.setMode("export");
    }

    @Test
    public void testGetForm() throws Exception {
        MvcResult result = mockMvc.perform(get("/questions/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testGetFormContainsCustomParameters() throws Exception {
        MvcResult result = mockMvc.perform(get("/questions/TestQuestionnaire")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is2xxSuccessful()))
                .andExpect(model().attribute("test", "pickles"))
                .andReturn();
    }


    @Test
    public void testPostDataFormFailsIfWrongSession() throws Exception {
        // TestUndeleteable is at task index 1, not task index 0.
        participant.getStudy().forceToSession("SessionOne");
        ResultActions result = mockMvc.perform(post("/questions/TestUndeleteable")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti"))
                .andExpect((status().is4xxClientError()));
    }

    @Test
    public void testPostDataForm() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/TestQuestionnaire")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti")
                .param("timeOnPage","9.999"))
                .andExpect((status().is3xxRedirection()));
    }

    @Test
    public void testPostInImportMode() throws Exception {
        this.importService.setMode("import");
        ResultActions result = mockMvc.perform(post("/questions/TestQuestionnaire")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti")
                .param("timeOnPage","9.999"))
                .andExpect((status().is4xxClientError()));
    }


    @Test
    public void testPostBadData() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/NoSuchForm")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
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
    }

    @Test
    public void testParticipantIsPopulatedIfItExists() throws Exception {
        // Force Participant to task 1 (which is TestUndeleteable)
//        participant.getStudy().getCurrentSession().setIndex(1);
        ((TestStudy) participant.getStudy()).setCurrentTaskIndex(1);
        participantRepository.save(participant);
        ResultActions result = mockMvc.perform(post("/questions/TestUndeleteable")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("value", "cheese")
                .param("timeOnPage", "99.9"))
                .andExpect((status().is3xxRedirection()));

        undeleteableRepository.flush();
        List<TestUndeleteable> dataList = undeleteableRepository.findAll();
        TestUndeleteable last = dataList.get(dataList.size() - 1);
        assertNotNull(last.getParticipant());

    }
}
