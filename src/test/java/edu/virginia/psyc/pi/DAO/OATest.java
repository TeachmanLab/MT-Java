package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.persistence.Questionnaire.DASS21_AS;
import edu.virginia.psyc.pi.persistence.Questionnaire.OA;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OATest {

    @Test
    public void testScore() {
        OA data1, data2, data3, data4;
        data1 = new OA(1,1,1,1,1);
        data2 = new OA(4,4,4,4,4);
        data3 = new OA(0,0,0,0,0);

        assertEquals(data1.score(), 1, 0);
        assertEquals(data2.score(), 4, 0);
        assertEquals(data3.score(), 0, 0);

    }

}
