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

        @Test
        public void testProgressWorse() {
            List<OA> list = new ArrayList<>();
            list.add(new OA(0,0,0,0,0));
            list.add(new OA(1,1,1,1,1));
            list.add(new OA(2,2,2,2,2));
            list.add(new OA(3,3,3,3,3));
            list.add(new OA(4,4,4,4,4));

            assertEquals(OA.progress(list), "worse");
        }

    @Test
    public void testProgressSame() {
        List<OA> list = new ArrayList<>();
        list.add(new OA(2,2,2,2,2));
        list.add(new OA(3,3,3,3,3));
        list.add(new OA(1,1,1,1,1));
        list.add(new OA(2,2,2,2,2));

        assertEquals(OA.progress(list), "same");
    }

    @Test
    public void testProgressBetter() {
        List<OA> list = new ArrayList<>();
        list.add(new OA(4,4,4,4,4));
        list.add(new OA(3,3,3,3,3));
        list.add(new OA(2,2,2,2,2));
        list.add(new OA(1,1,1,1,1));
        list.add(new OA(0,0,0,0,0));

        assertEquals(OA.progress(list), "little better");
    }

    @Test
    public void testProgressLotBetter() {
        List<OA> list = new ArrayList<>();
        list.add(new OA(4,4,4,4,4));
        list.add(new OA(0,0,0,0,0));

        assertEquals(OA.progress(list), "lot better");
    }


}
