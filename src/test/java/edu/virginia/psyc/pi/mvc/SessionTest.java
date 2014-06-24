package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.domain.Session;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Checks that we can create a List of Session objects that will work correctly in the view layer.
 */
public class SessionTest {

    @Test
    public void testCreateSessionListView() {

        List<Session> sessionList;

        sessionList = Session.createListView(Session.NAME.WEEK1A);

        assertEquals(10, sessionList.size());  // Should be one less, since not all are displayable.
        assertEquals("incorrect order.", Session.NAME.PRE, sessionList.get(0).getName());
        assertEquals("incorrect order.", Session.NAME.POST, sessionList.get(9).getName());

        // Complete is marked appropriately.
        assertTrue(sessionList.get(0).isComplete());
        assertFalse(sessionList.get(1).isComplete());
        assertFalse(sessionList.get(2).isComplete());
        assertFalse(sessionList.get(3).isComplete());
        assertFalse(sessionList.get(4).isComplete());
        assertFalse(sessionList.get(5).isComplete());

        // current is clrrectly identified and set.
        assertTrue(sessionList.get(1).isCurrent());
        assertFalse(sessionList.get(0).isCurrent());
        assertFalse(sessionList.get(2).isCurrent());
        assertFalse(sessionList.get(3).isCurrent());
        assertFalse(sessionList.get(4).isCurrent());
        assertFalse(sessionList.get(5).isCurrent());

        // Groups are correctly set
        assertEquals("pre", sessionList.get(0).getGroup());
        assertEquals("week1", sessionList.get(1).getGroup());
        assertEquals("week1", sessionList.get(2).getGroup());
        assertEquals("week2", sessionList.get(3).getGroup());
        assertEquals("week2", sessionList.get(4).getGroup());
        assertEquals("week3", sessionList.get(5).getGroup());
    }


}