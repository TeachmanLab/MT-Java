package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.CBMNeutralStudy;
import edu.virginia.psyc.r34.domain.PiParticipant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class R34ParticipantServiceTest {

    @Autowired
    R34ParticipantService service;


    @Test
    public void testNewParticipantGetsRandomCBMCondition() {

        PiParticipant p;
        boolean is50 = false;
        boolean isPos = false;
        boolean isNeutral = false;

        p = service.create();
        assertNotNull(p.getCbmCondition());

        for(int i = 0; i< 100; i++)  {
            p = service.create();
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

        p = service.create();

        assertNotNull(p.getPrime());

        for(int i = 0; i< 100; i++)  {
            p = service.create();
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
            p = service.create();
            if(p.getCbmCondition().equals(PiParticipant.CBM_CONDITION.NEUTRAL)) {
                isNeutral = true;
                assertTrue(p.getStudy() instanceof CBMNeutralStudy);
            }
        }

        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);
    }


}
