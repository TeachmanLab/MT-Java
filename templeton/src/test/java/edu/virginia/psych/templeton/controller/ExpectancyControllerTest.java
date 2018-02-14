package edu.virginia.psych.templeton.controller;

import edu.virginia.psyc.templeton.Application;
import edu.virginia.psyc.templeton.controller.ExpectancyController;
import edu.virginia.psyc.templeton.domain.TempletonStudy;
import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import edu.virginia.psyc.templeton.persistence.ExpectancyBiasRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.internet.MimeMessage;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ExpectancyControllerTest extends BaseControllerTest {

    @Autowired
    private ExpectancyBiasRepository ebRepository;

    private Wiser wiser;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ExpectancyController ebController;


    @Override
    public Object[] getControllers() {
        return (new Object[]{ebController});
    }


    @Before
    public void setUp() throws Exception {
        wiser = new Wiser();
        wiser.setPort(1025);
        wiser.start();
    }

    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    @Test
    public void testGetFormContainsCustomParameters() throws Exception {
        ((TempletonStudy) participant.getStudy()).setCurrentSession(TempletonStudy.SECOND_SESSION.toString());
        participantRepository.save(participant);

        MvcResult result = mockMvc.perform(get("/questions/ExpectancyBias")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testPostDataForm() throws Exception {

        assertEquals(0, ebRepository.findAll().size());
        // Set the task index to Expectancy Bias
        TempletonStudy study = ((TempletonStudy) participant.getStudy());
        study.setCurrentSession(TempletonStudy.SECOND_SESSION);
        ((TempletonStudy) participant.getStudy()).setCurrentTaskIndex(3);
        participantRepository.save(participant);
        ResultActions result = mockMvc.perform(post("/questions/ExpectancyBias")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("timeOnPage", "1000")
                .param("ShortRest", "0")
                .param("Reruns", "0")
                .param("VerySick", "0")
                .param("Bagel", "0")
                .param("SettleIn", "0")
                .param("Offend", "0")
                .param("Lunch", "0")
                .param("ConsideredAdvancement", "0")
                .param("Stuck", "0")
                .param("Thermostat", "0")
                .param("FinanciallySecure", "0")
                .param("Ruining", "0"))
                .andExpect((status().is3xxRedirection()));
        assertEquals(1, ebRepository.findAll().size());
    }

    @Test
    public void testAtRisk() throws Exception {
        assertEquals(0, ebRepository.findAll().size());

        // Create an initial that is very positive.
        ExpectancyBias initial = new ExpectancyBias();
        initial.setShortRest(5);
        initial.setSettleIn(5);
        initial.setConsideredAdvancement(5);
        initial.setFinanciallySecure(5);
        initial.setParticipant(participant);
        initial.setDate(new Date());
        ebRepository.save(initial);
        ebRepository.flush();

        // Post a new EB that is very negative.
        TempletonStudy study = ((TempletonStudy) participant.getStudy());
        study.setCurrentSession(TempletonStudy.SECOND_SESSION);
        ((TempletonStudy) participant.getStudy()).setCurrentTaskIndex(3);
        participantRepository.save(participant);
        ResultActions result = mockMvc.perform(post("/questions/ExpectancyBias")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(SecurityMockMvcRequestPostProcessors.user(participant))
                .param("timeOnPage", "1000")
                .param("ShortRest", "1")
                .param("Reruns", "1")
                .param("VerySick", "5")
                .param("Bagel", "1")
                .param("SettleIn", "1")
                .param("Offend", "5")
                .param("Lunch", "1")
                .param("ConsideredAdvancement", "1")
                .param("Stuck", "5")
                .param("Thermostat", "1")
                .param("FinanciallySecure", "1")
                .param("Ruining", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/session/atRisk?visiting=false"));
        assertEquals(2, ebRepository.findAll().size());

        assertEquals(1, wiser.getMessages().size());
    }
}