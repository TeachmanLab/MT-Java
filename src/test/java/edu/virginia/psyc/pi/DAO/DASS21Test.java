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

}
