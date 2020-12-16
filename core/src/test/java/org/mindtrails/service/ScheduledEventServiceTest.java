package org.mindtrails.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestDevice;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Scheduled.*;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.TaskLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.persistence.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.Wiser;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by dan on 8/4/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ScheduledEventServiceTest {

    @Value("${server.url}")
    private String serverUrl;

    private Wiser wiser;

    @Autowired
    private ScheduledEventService scheduledEventService;


    @Autowired
    private EmailService emailService;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private TwilioService twilioService;

    Participant participant;

    @Before
    public void setUp() throws Exception {
        participant = new Participant();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setStudy(new TestStudy());
        participant.setLastLoginDate(xDaysAgo(20));
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
    public void ifTimeIsCloseItIsTimeToSend() throws Exception {

        // Set the time of day to notify to be right now.
        scheduledEventService.setNotifyHour(DateTime.now().getHourOfDay());
        scheduledEventService.setNotifyMinute(DateTime.now().getMinuteOfHour());


        assert(scheduledEventService.timeToSendMessage(participant));

        // But not if their timezone is different.
        participant.setTimezone("America/Anchorage");
        assertFalse(scheduledEventService.timeToSendMessage(participant));

        // But this timezone should be all good.
        participant.setTimezone(TimeZone.getDefault().getID());
        assert(scheduledEventService.timeToSendMessage(participant));


    }

    @Test
    public void testShouldNotFireEventOnCreation() {
        participant.setEmail("TestySendEmailOnCreation@test.com");
        participant.setLastLoginDate(xDaysAgo(20));
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
    }

    @Test
    public void testShouldSendEventAfterLogin() {
        // No email the day after login, even if no sessions were completed.
        participant.setEmail("TestEventAfterLogin@test.com");
        participant.setLastLoginDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        // Send an email two days after login, if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participantRepository.save(participant);
        assertThat("day2", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        // Don't send an email two days after login, if a session was completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participant.getStudy().setLastSessionDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
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
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        // But a GIDI study should go out...
        study.setStudyExtension("gidi");
        study.setLastSessionDate(xDaysAgo(1));
        assertThat("gidiOnly", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));
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
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        log.setDateCompleted(xDaysAgo(5));
        taskLogRepository.save(log);
        assertThat("afterCompleted", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));
    }

    @Test
    public void sinceCompletionEmailsFireOnceAndOnlyOnce() {
        Study study = participant.getStudy();
        participant.setEmail("sinceCompletionEmailsFireOnceAndOnlyOnce@test.com");

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        // Complete the two test tasks, to get us to session 2.
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        study.completeCurrentTask(1, new TestDevice(), "blahblah");
        participantRepository.save(participant);
        TaskLog log = taskLogRepository.findByStudyAndSessionNameAndTaskName(study, "sessionOne", TaskLog.SESSION_COMPLETE);
        log.setDateCompleted(xDaysAgo(5));
        taskLogRepository.save(log);
        Email email = (Email)scheduledEventService.getEventsForParticipant(participant).get(0);
        assertEquals("afterCompleted", email.getType());
        participant.addEmailLog( new EmailLog(email));
        participantRepository.save(participant);


        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

    }



    @Test
    public void testShouldSendEmailAfter3_7_11_15_and_18() {

        Study study = participant.getStudy();
        participant.setEmail("TestyMycTesterAfter_3_7@test.com");

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(2));
        assertThat("day2", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(3));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(4));
        assertThat("day4", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(5));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(6));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(7));
        assertThat("day7", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(8));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(9));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(10));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(11));
        assertThat("day11", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(12));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(13));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(14));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(15));
        assertThat("day15", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(16));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(17));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(18));
        assertThat("closure", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

        study.setLastSessionDate(xDaysAgo(19));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(20));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());

        study.setLastSessionDate(xDaysAgo(100));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
    }

    @Test
    public void testShouldNotSendEmailAfter3_7_11_15_and_18_postSession8() {

        // Set up the sessions so we are starting the post session.
        Study study = new TestStudy("PostSession", 0);
        participant.setEmail("TestyMcNotPost@test.com");
        participant.setStudy(study);
        participantRepository.save(participant);
        study.setLastSessionDate(xDaysAgo(2));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        study.setLastSessionDate(xDaysAgo(4));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        study.setLastSessionDate(xDaysAgo(7));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        study.setLastSessionDate(xDaysAgo(11));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        study.setLastSessionDate(xDaysAgo(15));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        study.setLastSessionDate(xDaysAgo(18));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
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
        assertThat("followup", is(equalTo(scheduledEventService.getEventsForParticipant(participant).get(0).getType())));

    }

    @Test
    public void testShouldNotSendEmailsEverIfInComplete() {
        Study study = new TestStudy("PostSession", 1);
        study.completeCurrentTask(10, null, "testing");
        participant.setStudy(study);
        participant.setEmail("postSessionNoNotifications");
        participantRepository.save(participant);
        assertTrue(study.completed("PostSession"));
        assertTrue(study.getState() == Study.STUDY_STATE.ALL_DONE);
        study.setLastSessionDate(xDaysAgo(2));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
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
        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, scheduledEventService.getEventsForParticipant(participant).size());
        Email email = (Email)events.get(0);
        assertNotNull(email.getCalendarDate());
        assertTrue(email.isIncludeCalendarInvite());
    }

    @Test
    public void testMessageSentIfEmailIsNotEnabled() {

        participant.setEmailReminders(true);
        participant.setLastLoginDate(xDaysAgo(7));
        participantRepository.save(participant);

        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof Email);

        participant.setEmailReminders(false);
        participant.setLastLoginDate(xDaysAgo(7));

        events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof TextMessage);
    }


    @Test
    public void testMessageDoesNotSendTwice() {
        participant.setEmail("testMessageDoesNotSendTwice@test.com");
        participant.setEmailReminders(false);
        participant.setLastLoginDate(xDaysAgo(7));
        participantRepository.save(participant);
        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof TextMessage);
        twilioService.sendMessage((TextMessage)events.get(0), participant);
        events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(0, events.size());
    }

    @Test
    public void testInactiveEventCorrectlyFires() {
        participant.setEmail("testInactiveEventCorrectlyFires@test.com");
        participant.setEmailReminders(false);
        participant.setPhoneReminders(false);
        participant.setLastLoginDate(xDaysAgo(18));
        participant.getStudy().setStudyExtension("tet");
        participantRepository.save(participant);
        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof MarkInactiveEvent);
        events.get(0).execute(participant, null, null);
        assertEquals(false, participant.isActive());
        events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(0, events.size());
    }

    @Test
    public void testForceSessionFires() {
        participant.setEmail("testForceSessionFires@test.com");
        participant.setEmailReminders(false);
        participant.setPhoneReminders(false);
        participant.setLastLoginDate(xDaysAgo(18));
        participant.getStudy().setStudyExtension("gidi"); // event is gidi only
        participantRepository.save(participant);
        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(1, events.size());
        assertTrue(events.get(0) instanceof ForceSessionEvent);
        events.get(0).execute(participant, null, null);
        assertEquals("PostSession", participant.getStudy().getCurrentSession().getName());
        assertEquals(30, participant.daysSinceLastMilestone());
        events = scheduledEventService.getEventsForParticipant(participant);
        assertEquals(0, events.size());
    }

    @Test
    public void testInvalidTimezoneUsesEST() {
        participant.setEmail("TestySendEmailOnCreation2@test.com");
        participant.setLastLoginDate(xDaysAgo(20));
        participant.setTimezone("thisAintRight");
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
    }




}