package edu.virginia.psyc.r34.DAO;

import edu.virginia.psyc.r34.persistence.Questionnaire.OA;
import org.junit.Test;

import static org.junit.Assert.*;

public class OATest {

    @Test
    // Score should be an average sum.  If a piece of data is left out,
    // then the score should average out that missing data.
    public void testScore() {
        OA data1, data2, data3, data4;
        data1 = new OA(1,1,1,1,1);
        data2 = new OA(4,4,4,4,4);
        data3 = new OA(0,0,0,0,0);
        data4 = new OA(4,4,4,555,4);

        assertEquals(data1.score(), 5, 0);
        assertEquals(data2.score(), 20, 0);
        assertEquals(data3.score(), 0, 0);
        assertEquals(data4.score(), 20, 0);

    }

}
