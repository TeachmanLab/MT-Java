package org.mindtrails.domain;

import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.tracking.EmailLog;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

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


    private Participant testParticipant() {
        DateTime dateTime;
        Participant p;

        dateTime = new DateTime().minus(Period.days(3));
        p = new Participant("Dan", "test@tester.org", false);
        TestStudy study = new TestStudy();
        study.setLastSessionDate(dateTime.toDate());
        p.setStudy(study);
        return(p);
    }

    @Test
    public void testParticipantKnowsDaysSinceLastMilestone() {

        Participant p = testParticipant();
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

        p = testParticipant();

        assertEquals(99, p.daysSinceLastEmail());

        dateTime = new DateTime().minus(Period.days(7));
        emailLog = new EmailLog(p, "Day 7", dateTime.toDate());
        p.addEmailLog(emailLog);

        assertEquals(7, p.daysSinceLastEmail());

        dateTime = new DateTime().minus(Period.days(18));
        emailLog = new EmailLog(p, "Day 18", dateTime.toDate());
        p.addEmailLog(emailLog);
        assertEquals(7, p.daysSinceLastEmail());

        dateTime = new DateTime().minus(Period.days(2));
        emailLog = new EmailLog(p, "Day 2", dateTime.toDate());
        p.addEmailLog(emailLog);
        assertEquals(2, p.daysSinceLastEmail());

    }

    @Test
    public void testParticipantKnowsDateOfLastMilestone() {
        DateTime dateTime;
        Date loginDate;
        TestStudy study;

        Participant p;
        p = testParticipant();
        study = new TestStudy();
        p.setStudy(study);
        study.setLastSessionDate(null);
        assertNull(p.lastMilestone());

        loginDate = new Date();
        p.setLastLoginDate(loginDate);
        assertNotNull("last login is used when last session date is missing.", p.lastMilestone());
        assertEquals(p.lastMilestone(), p.getLastLoginDate());

        dateTime = new DateTime().minus(Period.days(3));
        study.setLastSessionDate(dateTime.toDate());

        assertSame("last login date is used if it is later then the session date.", p.lastMilestone(), loginDate);

        dateTime = new DateTime().minus(Period.days(4));
        p.setLastLoginDate(dateTime.toDate());
        assertSame("last session date is used if it is later then the login date.", p.lastMilestone(), study.getLastSessionDate());

    }




    @Test
    public void testParticipantKnowsIfEmailOfTypeSentPreviously() {
        Participant p;
        Study study;
        p = new Participant("Dan Funk", "daniel.h.funk@gmail.com", false);
        study = new TestStudy();
        p.setStudy(study);
        assertFalse(p.previouslySent("followup"));
        p.addEmailLog(new EmailLog(p,"followup", new Date()));
        assertTrue(p.previouslySent("followup"));
    }


}
