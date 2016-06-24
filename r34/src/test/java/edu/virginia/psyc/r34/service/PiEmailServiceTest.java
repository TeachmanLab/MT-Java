package edu.virginia.psyc.r34.service;

import org.mindtrails.domain.Study;
import org.mindtrails.domain.tracking.TaskLog;
import edu.virginia.psyc.r34.domain.CBMStudy;
import edu.virginia.psyc.r34.domain.PiParticipant;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PiEmailServiceTest {

    PiParticipant participant;
    PiEmailService service;

    @Before
    public void setup() {
        participant = new PiParticipant("Dan Funk", "daniel.h.funk@gmail.com", false);
        service     = new PiEmailService();
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
        assertNull(service.getEmailToSend(participant));
    }

    @Test
    public void testShouldSendEmailAfterLogin() {
        // No email the day after login, even if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(1));
        assertNull(service.getEmailToSend(participant));

        // Send an email two days after login, if no sessions were completed.
        participant.setLastLoginDate(xDaysAgo(2));
        assertEquals(PiEmailService.TYPE.day2, service.getEmailToSend(participant));

        // Don't send an email two days after login, if a session was completed.
        participant.setLastLoginDate(xDaysAgo(2));
        participant.getStudy().setLastSessionDate(xDaysAgo(1));
        assertNull(service.getEmailToSend(participant));
    }

    @Test
    public void testShouldSendEmailAfter3_7_11_15_and_18() {

        Study study = participant.getStudy();

        // Send emails on the correct days, but not on any other days.
        study.setLastSessionDate(xDaysAgo(1));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(2));
        assertEquals(PiEmailService.TYPE.day2, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(3));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(4));
        assertEquals(PiEmailService.TYPE.day4, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(5));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(6));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(7));
        assertEquals(PiEmailService.TYPE.day7, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(8));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(9));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(10));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(11));
        assertEquals(PiEmailService.TYPE.day11, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(12));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(13));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(14));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(15));
        assertEquals(PiEmailService.TYPE.day15, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(16));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(17));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(18));
        assertEquals(PiEmailService.TYPE.day18, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(19));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(20));
        assertNull(service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(100));
        assertNull(service.getEmailToSend(participant));

    }


    @Test
    public void testShouldNotSendEmailAfter3_7_11_15_and_18_postSession8() {

        // Set up the sessions so we are past Session 8, but not finished with the Post session.
        Study study = new CBMStudy(CBMStudy.NAME.POST.toString(), 0, new Date(), new ArrayList<TaskLog>(), false);
        participant.setStudy(study);

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
    public void testShouldSendEmailAfter60_63_67_75_onSession8() {
        // Set up the sessions so we are past Session 8, but not finished with the Post session.
        // Set up the sessions so we are past Session 8, but not finished with the Post session.

        Study study = new CBMStudy(CBMStudy.NAME.POST.toString(), 0, new Date(), new ArrayList<TaskLog>(), false);
        participant.setStudy(study);

        study.setLastSessionDate(xDaysAgo(60));
        assertEquals(PiEmailService.TYPE.followup, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(63));
        assertEquals(PiEmailService.TYPE.followup2, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(67));
        assertEquals(PiEmailService.TYPE.followup2, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(70));
        assertEquals(PiEmailService.TYPE.followup2, service.getEmailToSend(participant));

        study.setLastSessionDate(xDaysAgo(75));
        assertEquals(PiEmailService.TYPE.followup3, service.getEmailToSend(participant));

    }

}
