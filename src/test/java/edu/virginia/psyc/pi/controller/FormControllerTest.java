package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.DAO.TestQuestionnaire;
import edu.virginia.psyc.pi.DAO.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.DAO.TestUndeleteable;
import edu.virginia.psyc.pi.DAO.TestUndeleteableRepository;
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
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FormControllerTest extends BaseControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private TestQuestionnaireRepository repository;

    @Autowired
    private TestUndeleteableRepository undeleteableRepository;

    @Autowired
    private FormController formController;

    @Autowired
    private ExportController exportController;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(formController,exportController)
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
        MvcResult result = mockMvc.perform(get("/questions/TestForm")
                .with(user(getUser())))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testPostDataForm() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/TestQuestionnaire")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(getUser()))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti"))
                .andExpect((status().is3xxRedirection()));
    }

    @Test
    public void testPostBadData() throws Exception {
        ResultActions result = mockMvc.perform(post("/questions/NoSuchForm")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user(getUser()))
                .param("value", "cheese")
                .param("multiValue", "cheddar")
                .param("multiValue", "havarti"))
                .andExpect((status().is4xxClientError()));
    }

    @Test
    public void testThatPostedDataIsStored() throws Exception {
        testPostDataForm();
        assertEquals("cheese", getLastQuestionnaire().getValue());
    }

    /** Make sure that when the data is exported, the values are correctly encoded
     * @throws Exception
     */
    @Test
    public void testThatPostedDataIsExportedAsJSon() throws Exception {
        testPostDataForm();
        repository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/TestQuestionnaire")
                .with(user(getAdmin())))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertEquals("Value is a top level object that contains the string 'cheese'", "cheese", actualObj.get(0).path("value").asText());
        System.out.println(actualObj);
    }

    @Test
    public void testMultiValueElementsCorrectlyPassedThrough() throws Exception {
        testPostDataForm();
        repository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/TestQuestionnaire")
                .with(user(getAdmin())))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertThat(actualObj.findValue("multiValue").isArray(), is(true));
        Iterator<JsonNode> values = actualObj.findValue("multiValue").iterator();
        assertThat(values.next().textValue(), is("cheddar"));
        assertThat(values.next().textValue(), is("havarti"));
        System.out.println(actualObj);
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
                .with(user(getUser()))
                .param("value", "cheese"))
                .andExpect((status().is3xxRedirection()));

        undeleteableRepository.flush();
        List<TestUndeleteable> dataList = undeleteableRepository.findAll();
        TestUndeleteable last = dataList.get(dataList.size() - 1);
        assertNotNull(last.getParticipantDAO());

    }
}
