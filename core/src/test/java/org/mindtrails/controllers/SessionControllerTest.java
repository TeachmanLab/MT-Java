package org.mindtrails.controllers;

import org.junit.Test;
import org.mindtrails.controller.SessionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SessionControllerTest extends BaseControllerTest {

    @Autowired
    private SessionController sessionController;

    @Override
    public Object[] getControllers() {
        return(new Object[]{sessionController});
    }

    @Test
    public void testGetFormWithValidUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/session")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is2xxSuccessful()))
                .andReturn();
    }

    @Test
    public void testGetFormWithInValidUserRedirects() throws Exception {
        MvcResult result = mockMvc.perform(get("/session"))
                .andExpect((status().is3xxRedirection()))
                .andReturn();
    }



}
