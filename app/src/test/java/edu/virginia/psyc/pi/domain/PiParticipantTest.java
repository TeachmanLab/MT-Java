package edu.virginia.psyc.pi.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 6/26/14
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class PiParticipantTest {

    @Test
    public void testNewParticipantGetsRandomCBMCondition() {

        PiParticipant p;
        boolean is50 = false;
        boolean isPos = false;
        boolean isNeutral = false;

        p = new PiParticipant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);

        assertNotNull(p.getCbmCondition());

        for(int i = 0; i< 100; i++)  {
            p = new PiParticipant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
            if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.FIFTY_FIFTY)) is50 = true;
            if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.POSITIVE)) isPos = true;
            if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.NEUTRAL)) isNeutral = true;
        }

        assertTrue("after 100 iterations, 50/50 should have occurred at least once", is50);
        assertTrue("after 100 iterations, Positive should have occurred at least once", isPos);
        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);

    }

    @Test
    public void testNewParticipantGetsRandomPrime() {

        PiParticipant p;
        boolean isAnxious = false;
        boolean isNeutral = false;

        p = new PiParticipant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);

        assertNotNull(p.getPrime());

        for(int i = 0; i< 100; i++)  {
            p = new PiParticipant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
            if(p.getPrime().equals(PiParticipant.PRIME.ANXIETY)) isAnxious = true;
            if(p.getPrime().equals(PiParticipant.PRIME.NEUTRAL)) isNeutral = true;
        }

        assertTrue("after 100 iterations, ANXIETY should have occurred at least once", isAnxious);
        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);

    }

    @Test
    public void testNeutralParticipantInNeutralStudy() {

        PiParticipant p;
        boolean isNeutral = false;

        for(int i = 0; i< 100; i++)  {
            p = new PiParticipant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
            if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.NEUTRAL)) {
                isNeutral = true;
                assertTrue(p.getStudy() instanceof CBMNeutralStudy);
            }
        }

        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);
    }


}
