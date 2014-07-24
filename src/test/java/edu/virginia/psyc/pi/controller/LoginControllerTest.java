package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class LoginControllerTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    protected ParticipantRepository participantRepository;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain).build();
    }

    @After
    public void teardown() {
        try {
            ParticipantDAO p = participantRepository.findByEmail("some_crazy2@email.com").get(0);
            participantRepository.delete(p);
            participantRepository.flush();
        } catch (IndexOutOfBoundsException ioe) {
            // participant doesn't exist, but that isn't an actual error.
        }

    }

    @Test
    public void testCreateAccountController() throws Exception {
        this.mockMvc.perform(post("/newParticipant")
                .param("fullName", "Dan Funk")
                .param("email", "some_crazy2@email.com")
                .param("unencodedPassword", "ereiamjh")
                .param("unencodedPassword2", "ereiamjh"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/session"));

        assert(participantRepository.findByEmail("some_crazy2@email.com").size() > 0);

    }

    @Test
    public void testLoginPostController() throws Exception {

        // This will create the user.
        testCreateAccountController();

        this.mockMvc.perform(post("/login")
                .param("username", "some_crazy2@email.com")
                .param("password", "ereiamjh"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }



}