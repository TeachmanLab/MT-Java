package org.mindtrails.service;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestDevice;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.*;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;
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
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class EmailServiceImplTest {

    @Value("${server.url}")
    private String serverUrl;

    private Wiser wiser;

    @Autowired
    private EmailServiceImpl service;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private TangoService tangoService;

    Participant participant;

    @Before
    public void setUp() throws Exception {
        wiser = new Wiser();
        wiser.setPort(1025);
        wiser.start();

        participant = new Participant();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setStudy(new TestStudy());
        participant.setLastLoginDate(xDaysAgo(20));
    }

    @After
    public void tearDown() throws Exception {
        wiser.stop();
    }

    private String getMsgContent(MimeMessage msg) throws Exception {
        return ((MimeMultipart)msg.getContent()).getBodyPart(0).getContent().toString();
    }

    @Test
    public void sendExportAlertEmail() throws Exception {

        this.service.sendAdminEmail("WICKED.","Something wicked this way comes");

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();

            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "MindTrails Alert! WICKED.", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertTrue(getMsgContent(msg).contains("Something wicked this way comes"));
            assertEquals("'To' address did not match", "test@test.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());


        }
    }

    @Test
    public void send2DayUsesSiteUrl() throws Exception {

        Email e = this.service.getEmailForType("day2");
        e.setTo("test@test.com");
        Participant p = new Participant();
        p.setStudy(new TestStudy());
        p.setEmail("test@test.com");
        e.setParticipant(p);
        e.setContext(new Context());
        Study s = new TestStudy("SessionOne",0);
        p.setStudy(s);
        this.service.sendEmail(e);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();
            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "A day 2 email", msg.getSubject());
            assertNotNull(serverUrl);
            assertFalse(serverUrl.isEmpty());
            assertTrue(getMsgContent(msg).contains(serverUrl));
        }
    }

    @Test
    public void sendPasswordReset() throws Exception {

        String email = "testyMcTester@t.com";
        String token = "1234ASBASDF1234ASDF";

        Participant p = new Participant();
        p.setStudy(new TestStudy());
        p.setEmail(email);
        p.setPasswordToken(new PasswordToken(p, new Date(), token));
        Study s = new TestStudy("SessionOne",0);
        p.setStudy(s);
        this.service.sendPasswordReset(p);

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
            assertTrue("Token is missing", getMsgContent(msg).contains(token));
        }
    }

    @Test
    @Ignore
    public void sendGiftCard() throws Exception {
        OrderResponse orderResponse;
        String url = "http://thisistheurl.com";
        orderResponse = new OrderResponse("1", "1234", "notagift", "success", true, url, 5);
        String email = "testyMcTesterGiftCard@t.com";
        Participant p = new Participant();
        Study s = new TestStudy("SessionOne",0);
        p.setStudy(s);
        p.setEmail(email);
        participantRepository.save(p);
        GiftLog log = tangoService.createGiftLogUnsafe(p, "sessionOne", 5);
        this.service.sendGiftCard(p, orderResponse, log);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();
            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "Your E-Gift Card!", msg.getSubject());
            assertEquals("'From' address did not match", "test@test.com", msg.getFrom()[0].toString());
            assertEquals("'To' address did not match", "testyMcTester2.0@t.com",
                    msg.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
            assertTrue("url is missing", getMsgContent(msg).contains(url));
        }

    }



    @Test
    public void testEmailList() {
        List<Email> emails = service.emailTypes();
        assertThat(emails, hasItem(Matchers.<Email>hasProperty("type", equalTo("resetPass"))));
        assertThat(emails, hasItem(Matchers.<Email>hasProperty("subject", equalTo("MindTrails - Account Request"))));
        assertThat(emails, hasItem(Matchers.<Email>hasProperty("days", equalTo(Collections.singletonList(7)))));
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
        participant.setEmail("TestySendEmailOnCreation@test.com");
        participantRepository.save(participant);
        assertNull(service.getEmailToSend(participant));
    }

    @Test
    public void testShouldSendEmail3HoursAfterLastTaskIfSessionNotCompleted() {

        assertFalse("New participants should not get mid-session reminders",
                this.service.shouldSendMidSessionReminder(participant));


        participant.getStudy().completeCurrentTask(0, null, "testing");

        assertFalse("Recently completed tasks should not result in mid-session reminders",
                this.service.shouldSendMidSessionReminder(participant));

        // Set the date of the last tasklog to 7 hours ago.
        TestStudy study = (TestStudy)participant.getStudy();
        TaskLog lastLog = study.getTaskLogs().get(study.getTaskLogs().size()-1);
        lastLog.setDateCompleted(DateTime.now().minusHours(4).toDate());
        assertFalse(study.getCurrentSession().isComplete());

        assertTrue("When the last completed task took place 7 hours ago, and the session is not" +
                        "complete, there should be a mid-session reminder.",
                this.service.shouldSendMidSessionReminder(participant));

        // Log the sending of a midSession Email, and try and send again.
        participant.addEmailLog(new EmailLog(participant, "midSessionStop", new Date()));

        assertFalse("Already sent an email about this, don't repeat it.",
                this.service.shouldSendMidSessionReminder(participant));

        participant.setEmailReminders(false);
        assertFalse ("Email reminders are off, so don't notify participant",
                this.service.shouldSendMidSessionReminder(participant));


        participant.setEmailReminders(true);
        Session session = study.getCurrentSession();
        study.completeCurrentTask(0, null, "testing");
        assertTrue(study.completed(session.getName()));
        assertFalse ("Participant completed both tasks in the session, so no email should be sent.",
                this.service.shouldSendMidSessionReminder(participant));

        study.forceToSession("SessionTwo");
        study.setLastSessionDate(DateTime.now().minusDays(3).toDate());
        study.completeCurrentTask(0, null, "testing");
        lastLog = study.getTaskLogs().get(study.getTaskLogs().size()-1);
        lastLog.setDateCompleted(DateTime.now().minusHours(7).toDate());
        assertTrue("Will send another email so long as it's a difference session..",
                this.service.shouldSendMidSessionReminder(participant));


    }

    @Test
    public void testMidSessionStopEmailContent() throws Exception {
        participant.getStudy().completeCurrentTask(0, null, "testing");
        // Set the date of the last tasklog to 7 hours ago.
        TestStudy study = (TestStudy)participant.getStudy();
        TaskLog lastLog = study.getTaskLogs().get(study.getTaskLogs().size()-1);
        lastLog.setDateCompleted(DateTime.now().minusHours(4).toDate());
        assertFalse(study.getCurrentSession().isComplete());


        this.service.sendMidSessionEmail(participant);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();

            // Content likely looks like this: ((MimeMultipart)msg.getContent()).getBodyPart(0).getContent()

            assertNotNull("message was null", msg);
            assertEquals("'Subject' did not match", "Incomplete Session Notice from the MindTrails Project Team", msg.getSubject());
            assertTrue("url is missing", getMsgContent(msg).contains("Our records indicate that you got part-way through a session"));
        }
    }


    @Test
    public void testShouldSendEmailAfterLogin() {
        // No email the day after login, even if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertNull(service.getEmailToSend(participant));

        // Send an email two days after login, if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participantRepository.save(participant);
        assertThat(service.getEmailForType("day2"), is(equalTo(service.getEmailToSend(participant))));


        // Don't send an email two days after login, if a session was completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participant.getStudy().setLastSessionDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertNull(service.getEmailToSend(participant));
    }

    @Test
    public void testShouldSendEmailForStudyExtension() {

        Study study = participant.getStudy();
        participant.setEmail("TestyMyStudyExtension@test.com");
        participantRepository.save(participant);
        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));

        // No email goes on out day one for studies that are not a part of a the GIDI extension
        study.setLastSessionDate(xDaysAgo(1));
        assertNull(service.getEmailToSend(participant));

        study.setStudyExtension("gidi");
        study.setLastSessionDate(xDaysAgo(1));
        assertThat("gidiOnly", is(equalTo(service.getEmailToSend(participant).getType())));

    }

    @Test
    public void testShouldSendEmailBasedOnDaysSinceSessionCompletion() {
        Study study = participant.getStudy();
        participant.setEmail("TestyMycTesterSinceSessionCompletion@test.com");

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        participantRepository.save(participant);
        TaskLog log = taskLogRepository.findByStudyAndSessionNameAndTaskName(study, "sessionOne", TaskLog.SESSION_COMPLETE);
        log.setDateCompleted(xDaysAgo(4));
        taskLogRepository.save(log);
        assertNull(service.getEmailToSend(participant));
        log.setDateCompleted(xDaysAgo(5));
        taskLogRepository.save(log);
        assertThat("afterCompleted", is(equalTo(service.getEmailToSend(participant).getType())));


    }

    @Test
    public void testShouldSendEmailAfter3_7_11_15_and_18() {

        Study study = participant.getStudy();
        participant.setEmail("TestyMycTesterAfter_3_7@test.com");

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(2));
        assertThat("day2", is(equalTo(service.getEmailToSend(participant).getType())));


        study.setLastSessionDate(xDaysAgo(3));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(4));
        assertThat("day4", is(equalTo(service.getEmailToSend(participant).getType())));

        study.setLastSessionDate(xDaysAgo(5));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(6));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(7));
        assertThat("day7", is(equalTo(service.getEmailToSend(participant).getType())));

        study.setLastSessionDate(xDaysAgo(8));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(9));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(10));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(11));
        assertThat("day11", is(equalTo(service.getEmailToSend(participant).getType())));

        study.setLastSessionDate(xDaysAgo(12));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(13));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(14));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(15));
        assertThat("day15", is(equalTo(service.getEmailToSend(participant).getType())));

        study.setLastSessionDate(xDaysAgo(16));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(17));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(18));
        assertThat("closure", is(equalTo(service.getEmailToSend(participant).getType())));

        study.setLastSessionDate(xDaysAgo(19));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(20));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(100));
        assertNull(service.getEmailToSend(participant));

    }


    @Test
    public void testShouldNotSendEmailAfter3_7_11_15_and_18_postSession8() {

        // Set up the sessions so we are starting the post session.
        Study study = new TestStudy("PostSession", 0);
        participant.setEmail("TestyMcNotPost@test.com");
        participant.setStudy(study);
        participantRepository.save(participant);
        study.setLastSessionDate(xDaysAgo(2));
        assertNull(service.getEmailToSend(participant));
        study.setLastSessionDate(xDaysAgo(4));
        assertNull(service.getEmailToSend(participant));
        study.setLastSessionDate(xDaysAgo(7));
        assertNull(service.getEmailToSend(participant));
        study.setLastSessionDate(xDaysAgo(11));
        assertNull(service.getEmailToSend(participant));
        study.setLastSessionDate(xDaysAgo(15));
        assertNull(service.getEmailToSend(participant));
        study.setLastSessionDate(xDaysAgo(18));
        assertNull(service.getEmailToSend(participant));

    }



    @Test
    public void testShouldSendEmailAfter60onPostSession() {
        // Set up the sessions so we are in a post session, but not finished with the Post session.
        Study study = new TestStudy("PostSession", 0);
        participant.setStudy(study);
        participant.setLastLoginDate(xDaysAgo(80));
        participant.setEmail("TestyAfter60@test.com");
        participantRepository.save(participant);
        study.setLastSessionDate(xDaysAgo(60));
        assertThat("followup", is(equalTo(service.getEmailToSend(participant).getType())));

    }

    @Test
    public void testShouldNotSendEmailsEverIfInComplete() {
        Study study = new TestStudy("PostSession", 1);
        study.completeCurrentTask(10, null, "testing");
        participant.setStudy(study);

        assertTrue(study.completed("PostSession"));
        assertTrue(study.getState() == Study.STUDY_STATE.ALL_DONE);
        study.setLastSessionDate(xDaysAgo(2));
        assertNull(service.getEmailToSend(participant));

    }

    @Test
    public void testCreateCalendarInvitation() {
        participant.setTimezone("America/Los_Angeles");
        LocalDateTime aDateTime = LocalDateTime.of(2018, Month.OCTOBER, 29, 8, 00, 00);
        Date date = java.util.Date.from(aDateTime.toInstant(ZoneOffset.UTC));

        Calendar calendar = this.service.getInvite(participant, date);
        String calText = calendar.toString();
        assertTrue(calText.contains("2018"));
        System.out.println(calendar);
        System.out.println(((VEvent) calendar.getComponents().get(0)).getProperties().getProperties("DTSTART").get(0));
        assertNotNull(((VEvent) calendar.getComponents().get(0)).getStartDate());  // should be 30 minutes
        assertNotNull(((VEvent) calendar.getComponents().get(0)).getEndDate());  // should be 30 minutes
    }

    @Test
    public void sendEmailWithCalendarInvite() throws Exception {

        participant.setTimezone("America/Los_Angeles");
        Email e = new Email("day2", "Testing Email with Calendar Invite");
        final Context ctx = new Context();
        e.setContext(ctx);
        e.setTo("daniel.h.funk@gamil.com");
        e.setCalendarDate(new Date());
        e.setParticipant(participant);
        this.service.sendEmail(e);

        // assert
        assertEquals("No mail messages found", 1, wiser.getMessages().size());

        if (wiser.getMessages().size() > 0) {
            WiserMessage wMsg = wiser.getMessages().get(0);
            MimeMessage msg = wMsg.getMimeMessage();

            assertNotNull("message was null", msg);

        }
    }


    @Test
    public void sendScheduledEmailWithCalendarInvite() throws Exception {


        participant.setTimezone("America/Los_Angeles");
        participant.setEmail("TestyMcCalendar@test.com");

        // Complete the first session (assumes there are two tasks in the test study)
        Study study = participant.getStudy();
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        participant.setReturnDate(new Date());
        participantRepository.save(participant);

        // Send emails on the correct days, but not on any other days.
        Email email = service.getEmailToSend(participant);
        assertNotNull(email);
        assertNotNull(email.getCalendarDate());
        assertTrue(email.isIncludeCalendarInvite());
    }


}