package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.domain.Task;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
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

        sessionList = Session.createListView(Session.NAME.SESSION1, 0);

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

        // current is correctly identified and set.
        assertTrue(sessionList.get(1).isCurrent());
        assertFalse(sessionList.get(0).isCurrent());
        assertFalse(sessionList.get(2).isCurrent());
        assertFalse(sessionList.get(3).isCurrent());
        assertFalse(sessionList.get(4).isCurrent());
        assertFalse(sessionList.get(5).isCurrent());

    }

    /**
     * A session should have a list of associated tasks.
     */
    @Test
    public void     testGetTasksForSession() {

        List<Task> tasks;

        tasks = Session.getTasks(Session.NAME.PRE, 1);

        assertNotNull(tasks);
        assertEquals("Pre should have six tasks.", 10, tasks.size());
        assertEquals("Unique name for the task should be DASS_21", "DASS21_AS", tasks.get(0).getName());
        assertEquals("First task should be named Status Questionnaire", "Status Questionnaire", tasks.get(0).getDisplayName());
        assertEquals("First task should point to the DASS21 questionniare", Task.TYPE.questions, tasks.get(0).getType());
        assertEquals("First task should point to the DASS21 questionniare","/questions/DASS21_AS", tasks.get(0).getRequestMapping());
        assertTrue("First task should not be completed",tasks.get(0).isComplete());
        assertFalse("First task should  be current", tasks.get(0).isCurrent());
        assertFalse("Second task should not be completed",tasks.get(1).isComplete());
        assertTrue("Second task should be current",tasks.get(1).isCurrent());

        Session s = new Session();
        s.setTasks(tasks);
        assertEquals("Second task is returned when current requested", tasks.get(1), s.getCurrentTask());

    }

}