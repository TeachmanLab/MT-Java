package org.mindtrails.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mindtrails.controller.AccountController;
import org.mindtrails.controller.AdminController;
import org.mindtrails.controller.LoginController;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/24/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */

public class CreateAccountControllerTest extends BaseControllerTest {

    private static final String PASSWD = "1234!@#$qwerQWER";

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    private AccountController accountController;

    @Override
    public Object[] getControllers() {
        return (new Object[]{accountController});
    }

    @Autowired
    private ImportService importService;

    @Before
    public void setMode() {
        this.importService.setMode("export");
    }

    @After
    public void teardown() {
        try {
            Participant p = participantRepository.findByEmail("some_crazy2@email.com");
            if(p != null) {
                participantRepository.delete(p);
                participantRepository.flush();
            }
        } catch (IndexOutOfBoundsException ioe) {
            // participant doesn't exist, but that isn't an actual error.
        }
    }

    @Test
    public void testCheckEligibilityForLoginForm() throws Exception {
        this.mockMvc.perform(get("/account/create")
                .param("username", "some_crazy2@email.com")
                .param("password", PASSWD))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/public/eligibility*"));
    }

    /**
     * It should be possible to create new users in import mode.  
     * @throws Exception
     */
    @Test
    public void createAccountInImportMode() throws Exception {
        this.importService.setMode("import");
        this.mockMvc.perform(post("/account/create")
                .param("fullName", "Dan Funk")
                .param("email", "some_crazy2@email.com")
                .param("password", PASSWD)
                .param("passwordAgain" +
                        "", PASSWD)
                .param("over18", "true")
                .param("recaptchaResponse", "someresponse"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void testCreateAccountController() throws Exception {
        Participant p;

        this.mockMvc.perform(post("/account/create")
                .param("fullName", "Dan Funk")
                .param("email", "some_crazy2@email.com")
                .param("password", PASSWD)
                .param("passwordAgain" +
                        "", PASSWD)
                .param("over18", "true")
                .param("recaptchaResponse", "someresponse"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account/theme"));

        p = participantRepository.findByEmail("some_crazy2@email.com");
        assert(p != null);
        assertNotNull(p.getLastLoginDate());

    }

    @Test
    public void testLoginPostController() throws Exception {

        Participant p;
        Date orig;

        // This will create the user.
        testCreateAccountController();
        p = participantRepository.findByEmail("some_crazy2@email.com");
        orig = p.getLastLoginDate();

        this.mockMvc.perform(post("/login")
                .param("username", "some_crazy2@email.com")
                .param("password", PASSWD))
                .andDo(print())
                .andExpect(status().is3xxRedirection());

        // logging in should update the lastLoginDate
        p = participantRepository.findByEmail("some_crazy2@email.com");
        assertNotSame(orig, p.getLastLoginDate());

    }



}