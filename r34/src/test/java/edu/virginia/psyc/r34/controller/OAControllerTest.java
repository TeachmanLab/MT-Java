package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.Questionnaire.OARepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
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

import static org.junit.Assert.assertEquals;
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

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;
    private Participant participant;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(oaController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .addFilters(this.springSecurityFilterChain)
                .build();
    }

    @Before
    public void veryifyParticipant() {
        participant = participantRepository.findByEmail("test@test.com");
        if(participant == null) participant = participantService.create();
        participant.setEmail("test@test.com");
        participant.setFullName("Tester McTestFace");
        participantRepository.save(participant);
    }


    @After
    public void cleanup() {
    //formRepository.deleteAll();
    }

    @Test
    public void testGetFormContainsCustomParameters() throws Exception {
        ((R34Study)participant.getStudy()).setCurrentSession(R34Study.NAME.SESSION2.toString());
        participantRepository.save(participant);

        MvcResult result = mockMvc.perform(get("/questions/OA")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is2xxSuccessful()))
                .andExpect(model().attribute("inSessions", true))
                .andReturn();
    }


    @Test
    public void testPostDataForm() throws Exception {

        assertEquals(0, oaRepository.findAll().size());
        // Set the task index to OA
        ((R34Study)participant.getStudy()).setCurrentTaskIndex(9);
        participantRepository.save(participant);
        ResultActions result = mockMvc.perform(post("/questions/OA")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("anxious_freq", "0")
                .param("anxious_sev", "0")
                .param("avoid", "0")
                .param("interfere", "0")
                .param("interfere_social", "0"))
                .andExpect((status().is3xxRedirection()));

        assertEquals(1, oaRepository.findAll().size());

    }
}
