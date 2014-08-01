package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AdminControllerTest {


    @Autowired
    private AdminController adminController;

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Mock
    private ParticipantRepository participantRepository;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;


    @Before
    public void setup() {

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in webapp-mode (same config as spring-boot)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    @Test
    public void testAdminListUsers() throws Exception {

        ParticipantDAO p1 = new ParticipantDAO( "Dan Funk", "daniel.h.funk@gmail.com", "bla", false);
        ParticipantDAO p2 = new ParticipantDAO( "John Smith", "john.h.funk@gmail.com", "bla2", false);
        ParticipantDAO p3 = new ParticipantDAO( "Frank Jones", "frank.h.funk@gmail.com", "bla3", false);

        when(participantRepository.findAll()).thenReturn(Arrays.asList(p1,p2,p3));

        // Assure we get redirected if we aren't an admin
        MvcResult result = mockMvc.perform(get("/admin"))
                                .andExpect((status().is3xxRedirection()))
                                .andReturn();

        // TODO:  re-request as an admin user, and verify content

//        String content = result.getResponse().getContentAsString();

//        assert(content.contains("daniel.h.funk@gmail.com"));

    }


}