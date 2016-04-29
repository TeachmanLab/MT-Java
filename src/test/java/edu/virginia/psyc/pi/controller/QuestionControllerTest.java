package edu.virginia.psyc.pi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_ASRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class QuestionControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Mock
    private DASS21_ASRepository repository;



    @Before
    public void setup() {

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    /**  This is how you should test with mockMvc - removing some old tests, don't want to loose this valid logic.
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
*/

    @Test
    public void testCreateForm() throws Exception {
        ParticipantDAO p1 = new ParticipantDAO("Dan Funk", "daniel.h.funk@gmail.com", "bla", false, "green");

        MvcResult result = mockMvc.perform(post("/questions/DASS21_AS"))
                            .andExpect((status().is3xxRedirection()))
                            .andReturn();

    }


}