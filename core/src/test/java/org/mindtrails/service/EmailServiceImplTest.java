package org.mindtrails.service;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Email;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tango.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by dan on 8/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EmailServiceImplTest {

    @Value("${server.url}")
    private String serverUrl;

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

        emailService.sendAdminEmail("WICKED.","Something wicked this way comes");

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();

            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "MindTrails Alert! WICKED.", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertTrue(msg.getContent().toString().contains("Something wicked this way comes"));
            assertEquals("'To' address did not match", "test@test.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());


        }
    }

    @Test
    public void send2DayUsesSiteUrl() throws Exception {

        Email e = emailService.getEmailForType(EmailService.TYPE.day2.toString());
        e.setTo("test@test.com");
        Participant p = new Participant();
        p.setEmail("test@test.com");
        e.setParticipant(p);
        e.setContext(new Context());
        emailService.sendEmail(e);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();
            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "Update from the MindTrails project team", msg.getSubject());
            assertNotNull(serverUrl);
            assertFalse(serverUrl.isEmpty());
            assertTrue(msg.getContent().toString().contains(serverUrl));
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

    Participant participant;
    EmailServiceImpl service;

    @Before
    public void setup() {
        participant = new Participant();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setStudy(new TestStudy());
        service     = new EmailServiceImpl();
    }

    @Test
    public void testEmailList() {
        List<Email> emails = service.emailTypes();
        assertThat(emails, hasItem(Matchers.<Email>hasProperty("type", equalTo("day2"))));
        assertThat(emails, hasItem(Matchers.<Email>hasProperty("subject", equalTo("Update from the MindTrails project team"))));
    }

    /**
     * Returns a date from i number of days ago.
     * @param i
     * @return
     */
    private Date xDaysAgo(int i) {
        return new DateTime().minus(Period.days(i)).toDate();
    }

    @Test
    public void testShouldSendEmailOnCreation() {
        // A new user that never logs in should not get an email.
        assertNull(service.getTypeToSend(participant));
    }

    @Test
    public void testShouldSendEmailAfterLogin() {
        // No email the day after login, even if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(1));
        assertNull(service.getTypeToSend(participant));

        // Send an email two days after login, if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(2));
        assertThat(EmailService.TYPE.day2, is(equalTo(service.getTypeToSend(participant))));


        // Don't send an email two days after login, if a session was completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participant.getStudy().setLastSessionDate(xDaysAgo(1));
        assertNull(service.getTypeToSend(participant));
    }

    @Test
    public void testShouldSendEmailAfter3_7_11_15_and_18() {

        Study study = participant.getStudy();

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(2));
        assertThat(EmailService.TYPE.day2, is(equalTo(service.getTypeToSend(participant))));


        study.setLastSessionDate(xDaysAgo(3));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(4));
        assertThat(EmailService.TYPE.day4, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(5));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(6));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(7));
        assertThat(EmailService.TYPE.day7, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(8));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(9));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(10));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(11));
        assertThat(EmailService.TYPE.day11, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(12));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(13));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(14));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(15));
        assertThat(EmailService.TYPE.day15, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(16));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(17));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(18));
        assertThat(EmailService.TYPE.day18, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(19));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(20));
        assertNull(service.getTypeToSend(participant));

        study.setLastSessionDate(xDaysAgo(100));
        assertNull(service.getTypeToSend(participant));

    }


    @Test
    public void testShouldNotSendEmailAfter3_7_11_15_and_18_postSession8() {

        // Set up the sessions so we are starting the post session.
        Study study = new TestStudy("PostSession", 0);
        participant.setStudy(study);

        study.setLastSessionDate(xDaysAgo(2));
        assertNull(service.getTypeToSend(participant));
        study.setLastSessionDate(xDaysAgo(4));
        assertNull(service.getTypeToSend(participant));
        study.setLastSessionDate(xDaysAgo(7));
        assertNull(service.getTypeToSend(participant));
        study.setLastSessionDate(xDaysAgo(11));
        assertNull(service.getTypeToSend(participant));
        study.setLastSessionDate(xDaysAgo(15));
        assertNull(service.getTypeToSend(participant));
        study.setLastSessionDate(xDaysAgo(18));
        assertNull(service.getTypeToSend(participant));

    }

    @Test
    public void testShouldSendEmailAfter60_63_67_75_onSession8() {
        // Set up the sessions so we are in a post session, but not finished with the Post session.
        Study study = new TestStudy("PostSession", 0);
        participant.setStudy(study);

        study.setLastSessionDate(xDaysAgo(60));
        assertThat(EmailService.TYPE.followup, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(63));
        assertThat(EmailService.TYPE.followup2, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(67));
        assertThat(EmailService.TYPE.followup2, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(70));
        assertThat(EmailService.TYPE.followup2, is(equalTo(service.getTypeToSend(participant))));

        study.setLastSessionDate(xDaysAgo(75));
        assertThat(EmailService.TYPE.followup3, is(equalTo(service.getTypeToSend(participant))));

    }


}