package edu.virginia.psyc.r34.controller;

import org.junit.Test;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dan on 10/23/15.
 */
public class EligibilityControllerTest extends BaseControllerTest {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EligibleController eligibleController;


    @Override
    public Object[] getControllers() {
        return (new Object[]{eligibleController});
    }

    @Test
    public void editStudy() throws Exception {
        mockMvc.perform(get("/public/eligibility"))
                .andExpect(status().isOk());
    }


}
