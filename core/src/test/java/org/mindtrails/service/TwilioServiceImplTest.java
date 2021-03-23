package org.mindtrails.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Created by dan on 8/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class TwilioServiceImplTest {

    @Autowired
    private TwilioService twilioService;

    Participant participant;


    @Before
    public void setUp() throws Exception {

        participant = new Participant();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setStudy(new TestStudy());
    }

    @Test
    public void sendExportAlertEmail() throws Exception {
       String test_message = "The 'To' number +99123412 is not a valid phone number.";
       String message = twilioService.redactPhone(test_message);
       assertEquals(message, "The 'To' number [REDACTED] is not a valid phone number.");
    }
}