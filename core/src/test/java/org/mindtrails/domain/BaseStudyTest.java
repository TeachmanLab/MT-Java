package org.mindtrails.domain;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;
import org.mindtrails.MockClasses.TestStudy;

import static org.junit.Assert.*;

/**
 * Created by dan on 7/8/16.
 */
public class BaseStudyTest {

    @Test
    public void testDefaultStateIsReady() {
        Study s = new TestStudy();
        assertTrue(s.getState().equals(Study.STUDY_STATE.READY));
    }

    //     public enum STUDY_STATE {READY, IN_PROGRESS, WAIT_A_DAY, WAIT_FOR_FOLLOWUP, ALL_DONE}

    @Test
    public void testInProgressWhenNotAtZeroIndex() {
        // Assumes that the TestStudy has two sessions, and each session has two tasks.
        Study s = new TestStudy();
        assertEquals(Study.STUDY_STATE.READY, s.getState());
        s.completeCurrentTask();
        assertEquals(Study.STUDY_STATE.IN_PROGRESS, s.getState());
    }

    @Test
    public void testWaitADay() {
        Study s = new TestStudy();
        assertEquals(Study.STUDY_STATE.READY, s.getState());
        s.completeCurrentTask();
        s.completeCurrentTask();
        assertEquals(Study.STUDY_STATE.WAIT, s.getState());
        s.setLastSessionDate(new DateTime().minus(Days.days(2)).toDate());
        assertTrue(s.getState().equals(Study.STUDY_STATE.READY));
        s.completeCurrentTask();
        assertEquals(Study.STUDY_STATE.IN_PROGRESS, s.getState());
    }

    @Test
    public void testAllDoneWhenLastSessionIsComplete() {
        Study s = new TestStudy();
        // Assumes that the TestStudy has two sessions, and each session has two tasks.
        for(int i=0; i<14; i++) {
            s.setLastSessionDate(new DateTime().minus(Days.days(2)).toDate());
            s.completeCurrentTask();
        }
        assertTrue(s.completed("SessionTwo"));
        assertEquals(Study.STUDY_STATE.ALL_DONE, s.getState());
    }

    @Test
    public void testLastSessionIsNOT_STARTEDIfThereIsnotOne() {
        Study s = new TestStudy();
        assertEquals("NOT_STARTED", s.getLastSession().getName());
        Session one = s.getCurrentSession();
        s.forceToSession("SessionTwo");
        assertEquals("SessionOne", s.getLastSession().getName());
    }


}
