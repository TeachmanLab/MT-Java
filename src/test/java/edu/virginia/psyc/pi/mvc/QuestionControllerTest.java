package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_ASRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
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
    private QuestionController questionController;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Mock
    private DASS21_ASRepository repository;



    @Before
    public void setup() {

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in webapp-mode (same config as spring-boot)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testCreateForm() throws Exception {
        ParticipantDAO p1 = new ParticipantDAO(1, "Dan Funk", "daniel.h.funk@gmail.com", "bla", false);

        MvcResult result = mockMvc.perform(post("/questions/DASS21_AS"))
                            .andExpect((status().isOk()))
                            .andReturn();

    }


}