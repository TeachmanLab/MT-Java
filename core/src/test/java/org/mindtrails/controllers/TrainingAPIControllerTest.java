package org.mindtrails.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.controller.ExportController;
import org.mindtrails.controller.TrainingAPIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by any on 6/19/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")



public class TrainingAPIControllerTest extends BaseControllerTest{
    @Autowired
    private TrainingAPIController trainingAPIController;
    @Override
    public Object[] getControllers() {
        return (new Object[]{trainingAPIController});
    }

    @Test
    public void testHello() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/trainingAPI")
                .with(user(admin)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("hello",content);




    }
}
