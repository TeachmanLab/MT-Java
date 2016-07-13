package org.mindtrails.controllers;

import org.junit.Test;
import org.mindtrails.controller.AdminController;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends BaseControllerTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private ParticipantService participantService;

    @Override
    public Object[] getControllers() {
        return(new Object[]{adminController});
    }

    @Test
    public void testAccessAdminAsNoOne() throws Exception {
        // Assure we get redirected if we aren't an admin
        mockMvc.perform(get("/admin"))
                .andExpect((status().is3xxRedirection()));
    }

    @Test
    public void testAccessAdminAsParticipant() throws Exception {
        // Assure we get redirected if we aren't an admin
        mockMvc.perform(get("/admin")
                .with(SecurityMockMvcRequestPostProcessors.user(participant)))
                .andExpect((status().is4xxClientError()));
    }

    @Test
    public void testAccessAdminAsAdmin() throws Exception {
        // Assure we get redirected if we aren't an admin
        mockMvc.perform(get("/admin")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect((status().is2xxSuccessful()));
    }

    @Test
    public void listParticipants() throws Exception {
        // Assure we get redirected if we aren't an admin
        createParticipant("Francis", "fff@ggg.com", false);
        createParticipant("Robert", "rrr@ggg.com", false);
        mockMvc.perform(get("/admin")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(status().isOk());
    }

    @Test
    public void tangoStatus() throws Exception {
        mockMvc.perform(get("/admin/tango")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("tango"))
                .andExpect(status().isOk());
    }

    @Test
    public void listSessions() throws Exception {
        mockMvc.perform(get("/admin/listSessions")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("sessions"))
                .andExpect(status().isOk());
    }

    @Test
    public void listEmails() throws Exception {
        mockMvc.perform(get("/admin/listEmails")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("emails"))
                .andExpect(status().isOk());
    }


}