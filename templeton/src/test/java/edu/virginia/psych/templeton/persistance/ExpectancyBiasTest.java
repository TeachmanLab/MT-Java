package edu.virginia.psych.templeton.persistance;

import edu.virginia.psyc.templeton.persistence.ExpectancyBias;
import org.junit.Before;
import org.junit.Test;

import static edu.virginia.psyc.templeton.persistence.ExpectancyBias.NO_ANSWER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dan on 8/19/16.
 */
public class ExpectancyBiasTest {


    private final ExpectancyBias bias = new ExpectancyBias();

    @Before
    public void setupAllSevens(){

        bias.setShortRest(7);
        bias.setSettleIn(7);
        bias.setConsideredAdvancement(7);
        bias.setFinanciallySecure(7);
        bias.setVerySick(7);
        bias.setOffend(7);
        bias.setStuck(7);
        bias.setRuining(7);
    }


    @Test
    public void testPositiveAverage() {

        assertEquals(7, bias.positiveAverage(), 0.0001);
        bias.setShortRest(1);
        assertNotEquals(7, bias.positiveAverage(), 0.0001);
        bias.setShortRest(NO_ANSWER);
        assertEquals(7, bias.positiveAverage(), 0.0001);

    }

    @Test
    public void testNegativeAverage() {

        assertEquals(7, bias.negativeAverage(), 0.0001);
        bias.setRuining(1);
        assertNotEquals(7, bias.negativeAverage(), 0.0001);
        bias.setRuining(NO_ANSWER);
        assertEquals(7, bias.negativeAverage(), 0.0001);

    }

    @Test
    public void testScore() {

        assertEquals(0, bias.score(), 0.0001);  // Everything is at seven, so diff of averages is 0.

        // slight positive bias
        bias.setVerySick(1);
        assertTrue("Slight positive bias not there.", bias.score() > 0);

        // back to zero, when positive and negative match up
        bias.setShortRest(1);
        assertEquals(0, bias.score(), 0.0001);  // Everything is at seven, so diff of averages is 0.

        // Having some low scores on a few negative answers should be
        // enough to be over the limit at 1.11111 towards the positive.
        bias.setSettleIn(7);
        bias.setStuck(1);
        bias.setRuining(1);
        assertTrue(bias.score() > 1.1111);

    }




}
