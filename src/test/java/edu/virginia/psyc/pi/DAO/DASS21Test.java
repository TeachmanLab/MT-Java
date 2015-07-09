package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/31/14
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DASS21Test {

    @Test
    public void testEligibility() {
        DASS21_AS data;

        data = new DASS21_AS(0,0,0,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,1,0,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,1,1,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,1,1,1,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,1,1,1,1,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,1,1,1,1,1,0);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(1,1,1,1,1,1,1);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(0,0,2,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(0,2,2,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(2,2,2,0,0,0,0);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(0,0,3,0,0,0,0);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(0,3,3,0,0,0,0);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(3,3,3,0,0,0,0);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(-1,-1,-1,-1,-1,-1,-1);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(-1,-1,-1,-1,-1,-1,-1);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(0,-1,-1,-1,-1,-1,-1);
        assertFalse(data.eligibleScore());

        data = new DASS21_AS(1,-1,-1,-1,-1,-1,-1);
        assertTrue(data.eligibleScore());

        data = new DASS21_AS(1,1,-1,-1,-1,-1,-1);
        assertTrue(data.eligibleScore());

    }

    @Test
    public void testScore() {
        DASS21_AS data1, data2, data3, data4;
        data1 = new DASS21_AS(1,1,1,1,1,1,1);
        data2 = new DASS21_AS(4,4,4,4,4,4,4);
        data3 = new DASS21_AS(0,0,0,0,0,0,0);

        assertEquals(data1.score(), 14, 0);
        assertEquals(data2.score(), 56, 0);
        assertEquals(data3.score(), 0, 0);
    }

    /**
     * Given a recent test scores, and original test score, and determines
     * if the users score is degrading enough to merit an email to
     * an administrator. (Score increases by 30%)
     */
    @Test
    public void testAtRisk() {
        DASS21_AS dataOrig, dataLatest;
        dataOrig = new DASS21_AS(1,1,1,1,1,1,0); // score of 12 (increase of 30% is 15.6)
        dataLatest = new DASS21_AS(1,1,1,1,1,1,1); // score of 14
        assertFalse(dataLatest.atRisk(dataOrig));
        dataLatest = new DASS21_AS(1,1,1,1,1,1,2); // score of 16
        assertTrue(dataLatest.atRisk(dataOrig));
    }


}
