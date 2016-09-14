package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.*;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Checks that we can create a List of Session objects that will work correctly in the view layer.
 */
public class CBMStudyTest {

    private CBMStudy study;
    private List<Session> sessionList;

    @Before
    public void setup() {
        study = new CBMStudy(CBMStudy.NAME.SESSION1.toString(), 0, new Date(), new ArrayList<TaskLog>(), false);
        sessionList = study.getSessions();
    }

    @Test
    public void testCreateSessionListView() {

        CBMStudy study;
        List<Session> sessionList;

        study = new CBMStudy(CBMStudy.NAME.SESSION1.toString(), 0, new Date(), new ArrayList<TaskLog>(), false);
        sessionList = study.getSessions();

        assertEquals(11, sessionList.size());  // Should be one less, since not all are displayable.
        assertEquals("incorrect order.", CBMStudy.NAME.PRE.toString(), sessionList.get(0).getName());
        assertEquals("incorrect order.", CBMStudy.NAME.POST.toString(), sessionList.get(9).getName());

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

        study = new CBMStudy(CBMStudy.NAME.PRE.toString(), 1, new Date(), new ArrayList<TaskLog>(), false);
        tasks = study.getCurrentSession().getTasks();

        assertNotNull(tasks);
        assertEquals("Pre should have eleven tasks.", 11, tasks.size());
        assertEquals("Unique name for the task should be credibility", "credibility", tasks.get(0).getName());
        assertEquals("First task should be named Consent to Participate", "Consent to participate", tasks.get(0).getDisplayName());
        assertEquals("First task should point to a questionniare", Task.TYPE.questions, tasks.get(0).getType());
        assertEquals("First task should point to the Credibility Assessment questionniare","/questions/credibility", tasks.get(0).getRequestMapping());
        assertTrue("First task should be completed",tasks.get(0).isComplete());
        assertFalse("First task should not be current", tasks.get(0).isCurrent());
        assertFalse("Second task should not be completed",tasks.get(1).isComplete());
        assertTrue("Second task should be current",tasks.get(1).isCurrent());

        Session s = new Session();
        s.setTasks(tasks);
        assertEquals("Second task is returned when current requested", tasks.get(1), s.getCurrentTask());

    }

    @Test
    public void testCompleteCurrentTask() {

        study = new CBMStudy(CBMStudy.NAME.PRE.toString(), 0, null, new ArrayList<TaskLog>(), false);

        assertEquals(CBMStudy.NAME.PRE.toString(), study.getCurrentSession().getName());
        assertEquals("credibility", study.getCurrentSession().getCurrentTask().getName());

        study.completeCurrentTask();

        assertEquals(CBMStudy.NAME.PRE.toString(), study.getCurrentSession().getName());
        assertEquals("demographics", study.getCurrentSession().getCurrentTask().getName());

        study.completeCurrentTask();

        assertEquals(CBMStudy.NAME.PRE.toString(), study.getCurrentSession().getName());
        assertEquals("MH", study.getCurrentSession().getCurrentTask().getName());

        assertNull(study.getLastSessionDate());

        // Move past all the tasks in Pre
        for(int i =0; i<9; i++) {
            study.completeCurrentTask();
        }
        assertEquals(CBMStudy.NAME.SESSION1.toString(), study.getCurrentSession().getName());
        assertNotNull("The last session date should get updated when completing a session.", study.getLastSessionDate());

        assertEquals("Task index is set to 0 when a completing a session.", 0, study.getCurrentTaskIndex());
        assertEquals("Task index is set to 0 when a completing a session.", 0, study.getCurrentSession().getCurrentTaskIndex());
    }

    @Test
    public void testSessionState() {

        study = new CBMStudy(CBMStudy.NAME.PRE.toString(), 0, new Date(), new ArrayList<TaskLog>(), false);

        // By default the session state should be ready
        assertEquals(Study.STUDY_STATE.READY, study.getState());

        // Complete the pre assessment
        for(int i=0; i < study.getCurrentSession().getTasks().size(); i++) {
            study.completeCurrentTask();
        }

        // State should still be ready ...
        assertEquals(Study.STUDY_STATE.READY, study.getState());

        // Complete the first session
        for(int i=0; i < study.getCurrentSession().getTasks().size(); i++) {
            study.completeCurrentTask();
        }

        // State should still be now be wait a day ...
        assertEquals(Study.STUDY_STATE.WAIT_A_DAY, study.getState());

        // If we modify the last session date to be one day ago, session
        // state should now be ready ...
        DateTime dt = new DateTime();
        DateTime yesterday = dt.minus(Period.days(1));
        study.setLastSessionDate(yesterday.toDate());

        // State should still be now be ready ...
        assertEquals(Study.STUDY_STATE.READY, study.getState());

    }

}