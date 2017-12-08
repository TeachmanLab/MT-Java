package edu.virginia.psyc.r34.controller;

import edu.virginia.psyc.r34.domain.R34Study;
import org.junit.Test;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
public class R34StudyControllerTest extends BaseControllerTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private R34StudyController r34StudyController;


    @Override
    public Object[] getControllers() {
        return (new Object[]{r34StudyController});
    }

    @Test
    public void editStudy() throws Exception {
        Participant p = createParticipant("John Doe", "john@doe.com", false);

        mockMvc.perform(get("/admin/study/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("participant"))
                .andExpect(model().attributeExists("r34StudyForm"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/study/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin))
                .param("condition", "FIFTY_FIFTY")
                .param("prime", "NEUTRAL"))
                .andExpect(status().is3xxRedirection());

        Participant p2 = participantRepository.findOne(p.getId());
        assertEquals("FIFTY_FIFTY", ((R34Study)p2.getStudy()).getConditioning().toString());
        assertEquals("NEUTRAL", ((R34Study)p2.getStudy()).getPrime().toString());

    }


}
