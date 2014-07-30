package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/26/14
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantTest {

    @Test
    public void testCompleteCurrentTask() {

        Participant p;
        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setSessions(Session.createListView(Session.NAME.PRE, 0));

        assertEquals(Session.NAME.PRE, p.getCurrentSession().getName());
        assertEquals("DASS21_AS", p.getCurrentSession().getCurrentTask().getName());

        p.completeCurrentTask();

        assertEquals(Session.NAME.PRE, p.getCurrentSession().getName());
        assertEquals("credibility", p.getCurrentSession().getCurrentTask().getName());

        p.completeCurrentTask();

        assertEquals(Session.NAME.PRE, p.getCurrentSession().getName());
        assertEquals("MH", p.getCurrentSession().getCurrentTask().getName());

        assertNull(p.getLastSessionDate());

        // Move past all the tasks in Session 1
        p.completeCurrentTask();
        p.completeCurrentTask();
        p.completeCurrentTask();
        p.completeCurrentTask();

        assertEquals(Session.NAME.SESSION1, p.getCurrentSession().getName());
        assertNotNull("The last session date should get updated when completing a session.", p.getLastSessionDate());

    }

    @Test
    public void testParticipantKnowsDaysSinceLastSession() {

        DateTime dateTime;

        Participant p;
        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);

        dateTime = new DateTime().minus(Period.days(3));
        p.setLastSessionDate(dateTime.toDate());
        assertEquals(3,p.daysSinceLastSession());

        /*
        DateTime dt = new DateTime(2005, 3, 26, 12, 0, 0, 0);
        DateTime plusPeriod = dt.plus(Period.days(1));
        DateTime plusDuration = dt.plus(new Duration(24L*60L*60L*1000L));
        */
    }

}
