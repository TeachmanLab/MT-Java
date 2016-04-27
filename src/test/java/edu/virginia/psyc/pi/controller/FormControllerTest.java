package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.forms.FormDAO;
import edu.virginia.psyc.pi.persistence.forms.FormRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FormControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private FormRepository formRepository;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() {

        // Process mock annotations
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();

        // **** DEFINITELY DO NOT LEAVE THIS IN HERE.
        // THIS IS BAD.  VERY VERY BAD! //
//        formRepository.deleteAll();

    }

    @After
    public void cleanup() {
    //formRepository.deleteAll();
    }

    @Test
    public void testGetForm() throws Exception {
        MvcResult result = mockMvc.perform(get("/forms/TestForm")
                .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testPostDataForm() throws Exception {

        ResultActions result = mockMvc.perform(post("/forms/TestForm")
                .with(user("admin").password("pass").roles("USER","ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("cheese", "yes"))
                .andExpect((status().is2xxSuccessful()));

    }

    @Test
    public void testThatPostedDataIsStoredAsJson() throws Exception {
        testPostDataForm();
        formRepository.flush();
        List<FormDAO> dataList = formRepository.findAll();
        FormDAO last = dataList.get(dataList.size() - 1);
        assertEquals("{\"cheese\":\"yes\"}", last.getJson());
    }

    /** Make sure that when the data is exported, that the json in the
     * data element is flattened out - so it appears a single chunk of
     * structured json, not an escaped string inside a json model.
     *
     * Basically we want this:  {'id':'1', 'cheese':'yes"}
     * and NOT this: {'id':'1',"formData":"{\"cheese\":\"yes\"}"}}
     *
     * @throws Exception
     */
    @Test
    public void testThatPostedDataIsExportedAsFlattedJSon() throws Exception {
        testPostDataForm();
        formRepository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/FormDAO")
                .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertEquals("Cheese should be an accessible json object in the export. ", "yes",actualObj.findValue("cheese").asText());
        assertEquals("Cheese is a top level object, not a child of 'json'", "yes", actualObj.get(0).path("cheese").asText());
        System.out.println(actualObj);
    }

    @Test
    public void testMultiValueElementsCorrectlyPassedThrough() throws Exception {


        formRepository.flush();
        MvcResult result = mockMvc.perform(get("/api/export/FormDAO")
                .with(user("admin").password("pass").roles("USER","ADMIN")))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(result.getResponse().getContentAsString());
        assertEquals("Cheese should be an accessible json object in the export. ", "yes",actualObj.findValue("cheese").asText());
        assertEquals("Cheese is a top level object, not a child of 'json'", "yes", actualObj.get(0).path("cheese").asText());
        System.out.println(actualObj);
    }

}
