package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.mindtrails.controller.QuestionController;
import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.MockClasses.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.MockClasses.TestUndeleteableRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.OARepository;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class OAControllerTest extends BaseControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Autowired
    private OARepository oaRepository;

    @Autowired
    private OAController oaController;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(oaController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .addFilters(this.springSecurityFilterChain)
                .build();
    }

    @After
    public void cleanup() {
    //formRepository.deleteAll();
    }

    @Test
    public void testGetFormContainsCustomParameters() throws Exception {
        MvcResult result = mockMvc.perform(get("/questions/OA")
                .with(SecurityMockMvcRequestPostProcessors.user(getUser())))
                .andExpect((status().is2xxSuccessful()))
                .andExpect(model().attribute("inSessions", false))
                .andReturn();
    }


    @Test
    public void testPostDataForm() throws Exception {

        assertEquals(0, oaRepository.findAll().size());

        ResultActions result = mockMvc.perform(post("/questions/OA")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(getUser()))
                .param("anxious_freq", "0")
                .param("anxious_sev", "0")
                .param("avoid", "0")
                .param("interfere", "0")
                .param("interfere_social", "0"))
                .andExpect((status().is3xxRedirection()));

        assertEquals(1, oaRepository.findAll().size());

    }
}
