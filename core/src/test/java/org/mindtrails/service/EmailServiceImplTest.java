package org.mindtrails.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.mindtrails.domain.tango.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.internet.MimeMessage;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by dan on 8/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EmailServiceImplTest {

    private Wiser wiser;

    @Autowired
    private EmailService emailService;

    @Before
    public void setUp() throws Exception {
        wiser = new Wiser();
        wiser.setPort(1025);
        wiser.start();
    }

    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    @Test
    public void sendExportAlertEmail() throws Exception {

        emailService.sendExportAlertEmail("Something wicked this way comes");

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();

            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "MindTrails - Export Failure!", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertEquals("'To' address did not match", "test@test.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        }
    }

    @Test
    public void sendPasswordReset() throws Exception {

        String email = "testyMcTester@t.com";
        String token = "1234ASBASDF1234ASDF";

        Participant p = new Participant();
        p.setEmail(email);
        p.setPasswordToken(new PasswordToken(p, new Date(), token));

        emailService.sendPasswordReset(p);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();
            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "MindTrails - Account Request", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertEquals("'To' address did not match", "testyMcTester@t.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
            assertTrue("Token is missing", msg.getContent().toString().contains(token));
        }
    }

    @Test
    public void sendGiftCard() throws Exception {
        Reward reward;
        String token, number, pin, url, eventnumber, email;
        token = "ABCD";
        number ="12345678910";
        pin = "ASDFJKL:";
        url = "google.com";
        eventnumber = "1111111111111111111111";
        reward = new Reward(token, number, pin, url, eventnumber);
        email = "testyMcTester2.0@t.com";

        Participant p = new Participant();
        p.setEmail(email);

        emailService.sendGiftCard(p,reward, 100);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();
            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "MindTrails - Your gift card!", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertEquals("'To' address did not match", "testyMcTester2.0@t.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
            assertTrue("url is missing", msg.getContent().toString().contains(url));
        }

    }

}