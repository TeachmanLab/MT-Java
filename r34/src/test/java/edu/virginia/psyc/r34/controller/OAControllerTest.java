package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.Questionnaire.OARepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class OAControllerTest extends BaseControllerTest {

    @Autowired
    private OARepository oaRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private OAController oaController;


    @Override
    public Object[] getControllers() {
        return (new Object[]{oaController});
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
                .param("interfere_social", "0")
                .param("timeOnPage", "10"))
                .andExpect((status().is3xxRedirection()));

        assertEquals(1, oaRepository.findAll().size());

    }
}
