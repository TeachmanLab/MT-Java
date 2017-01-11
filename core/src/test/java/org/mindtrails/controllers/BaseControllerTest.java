package org.mindtrails.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.forms.ParticipantCreateAdmin;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ParticipantService;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;


/**
 * Provides the common tools for handling spring security to calls can
 * be made using the mockMvc object that will correctly authenticate against
 * the endpoint.
 *
 * You will need to implement the getControllers method to return an array
 * of controllers you will be testing against.
 *
 * To run a test, do something like the following:
 * MvcResult result = mockMvc.perform(get("/session")
       .with(SecurityMockMvcRequestPostProcessors.user(participant)))
       .andExpect((status().is2xxSuccessful()))
       .andReturn();
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @Autowired
    protected FilterChainProxy springSecurityFilterChain;
    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected ParticipantService participantService;

    protected Participant participant;

    protected Participant admin;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(getControllers())
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .addFilters(this.springSecurityFilterChain)
                .build();
    }



    /**
     * Returns a array of controllers that should be loaded into
     * context for running tests against.
     * @return
     */
    public abstract Object[] getControllers();

    public Participant createParticipant(String name, String email, boolean admin) {
        Participant p;
        p = participantRepository.findByEmail(email);
        if(p == null) {
            p = participantService.create();
            p.setFullName(name);
            p.setEmail(email);
            p.setAdmin(admin);
        }
        participantRepository.save(p);
        return p;
    }

    @Before
    public void establishParticipants() {
        participant = createParticipant("john", "test@test.com", false);
        admin = createParticipant("J McAdmin", "admin@test.com", true);

        // Reset progress for participant
        TestStudy s = (TestStudy)participant.getStudy();
        s.setCurrentSession("SessionOne");
        s.setCurrentTaskIndex(0);
        participantService.save(participant);
    }



}
