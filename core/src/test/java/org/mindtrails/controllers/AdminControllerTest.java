package org.mindtrails.controllers;

import org.junit.Test;
import org.mindtrails.controller.AdminController;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends BaseControllerTest {

    @Autowired
    private AdminController adminController;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public Object[] getControllers() {
        return (new Object[]{adminController});
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

    @Test
    public void editParticipant() throws Exception {
        Participant p = createParticipant("John Doe", "john@doe.com", false);

        mockMvc.perform(get("/admin/participant/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("participant"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/participant/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin))
                .param("fullName", "John Q. Doe")
                .param("email", "new@email.com"));

        Participant p2 = participantRepository.findOne(p.getId());
        assertEquals("John Doe", p2.getFullName());
        assertEquals("new@email.com", p2.getEmail());

    }

    @Test
    public void createParticipant() throws Exception {
        mockMvc.perform(get("/admin/participant/")
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("participantCreateAdmin"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/participant/")
                .with(SecurityMockMvcRequestPostProcessors.user(admin))
                .param("fullName", "John Q. Doe")
                .param("email", "a_new_admin@test.com")
                .param("password", "1234!@#Abc")
                .param("passwordAgain", "1234!@$Abc"));

    }

    @Test
    public void editParticipantWithPhoneGivesFormattedPhone() throws Exception {
        Participant p = createParticipant("John Phone Bone", "jpb@doe.com", false);

        mockMvc.perform(get("/admin/participant/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin)))
                .andExpect(model().attributeExists("participant"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/admin/participant/" + p.getId())
                .with(SecurityMockMvcRequestPostProcessors.user(admin))
                .param("fullName", "John Phone Bone")
                .param("email", "jpb@doe.com")
                .param("phone", "(540) 457.0024"));

        Participant p2 = participantRepository.findOne(p.getId());
        assertEquals("+15404570024", p2.getPhone());


    }



}