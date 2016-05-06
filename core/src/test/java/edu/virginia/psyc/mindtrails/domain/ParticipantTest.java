package edu.virginia.psyc.mindtrails.domain;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;
import edu.virginia.psyc.mindtrails.domain.participant.EmailLog;
import edu.virginia.psyc.mindtrails.domain.participant.TaskLog;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/26/14
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantTest {


    private TestStudy testStudy() {
        DateTime dateTime;
        dateTime = new DateTime().minus(Period.days(3));
        return new TestStudy("My Test Study", 0, dateTime.toDate(), new ArrayList<TaskLog>());
    }

    @Test
    public void testParticipantKnowsDaysSinceLastMilestone() {

        Participant p;
        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setStudy(testStudy());
        assertEquals(3,p.daysSinceLastMilestone());

        /*
        DateTime dt = new DateTime(2005, 3, 26, 12, 0, 0, 0);
        DateTime plusPeriod = dt.plus(Period.days(1));
        DateTime plusDuration = dt.plus(new Duration(24L*60L*60L*1000L));
        */

    }

    @Test
    public void testParticipantKnowsDaysSinceLastEmail() {

        Participant p;
        DateTime    dateTime;
        EmailLog emailLog;

        dateTime = new DateTime().minus(Period.days(7));
        emailLog = new EmailLog("Day 7", dateTime.toDate());

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        assertEquals(99, p.daysSinceLastEmail());

        p.addEmailLog(emailLog);
        assertEquals(7, p.daysSinceLastEmail());

        dateTime = new DateTime().minus(Period.days(18));
        emailLog = new EmailLog("Day 18", dateTime.toDate());
        p.addEmailLog(emailLog);
        assertEquals(7, p.daysSinceLastEmail());

        dateTime = new DateTime().minus(Period.days(2));
        emailLog = new EmailLog("Day 2", dateTime.toDate());
        p.addEmailLog(emailLog);
        assertEquals(2, p.daysSinceLastEmail());


    }

    @Test
    public void testParticipantKnowsDateOfLastMilestone() {
        DateTime dateTime;
        Date loginDate;
        TestStudy study;

        study    = testStudy();
        study.setLastSessionDate(null);

        Participant p;
        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setStudy(study);
        assertNull(p.lastMilestone());

        loginDate = new Date();
        p.setLastLoginDate(loginDate);
        assertNotNull(p.lastMilestone());

        dateTime = new DateTime().minus(Period.days(3));
        study.setLastSessionDate(dateTime.toDate());

        p.setStudy(testStudy());

        assertNotSame(p.lastMilestone(), loginDate);
    }

    @Test
    public void testParticipantKnowsIfEmailOfTypeSentPreviously() {
        Participant p;

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        assertFalse(p.previouslySent("followup"));
        p.addEmailLog(new EmailLog("followup", new Date()));
        assertTrue(p.previouslySent("followup"));

    }


}
