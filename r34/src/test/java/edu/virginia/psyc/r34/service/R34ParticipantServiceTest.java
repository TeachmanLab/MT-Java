package edu.virginia.psyc.r34.service;

import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.R34NeutralStudy;
import edu.virginia.psyc.r34.domain.R34Participant;
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

        R34Participant p;
        boolean is50 = false;
        boolean isPos = false;
        boolean isNeutral = false;

        p = service.create();
        assertNotNull(p.getCondition());

        for(int i = 0; i< 100; i++)  {
            p = service.create();
            if(p.getCondition().equals(R34Participant.CONDITION.FIFTY_FIFTY)) is50 = true;
            if(p.getCondition().equals(R34Participant.CONDITION.POSITIVE)) isPos = true;
            if(p.getCondition().equals(R34Participant.CONDITION.NEUTRAL)) isNeutral = true;
        }

        assertTrue("after 100 iterations, 50/50 should have occurred at least once", is50);
        assertTrue("after 100 iterations, Positive should have occurred at least once", isPos);
        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);

    }

    @Test
    public void testNewParticipantGetsRandomPrime() {

        R34Participant p;
        boolean isAnxious = false;
        boolean isNeutral = false;

        p = service.create();

        assertNotNull(p.getPrime());

        for(int i = 0; i< 100; i++)  {
            p = service.create();
            if(p.getPrime().equals(R34Participant.PRIME.ANXIETY)) isAnxious = true;
            if(p.getPrime().equals(R34Participant.PRIME.NEUTRAL)) isNeutral = true;
        }

        assertTrue("after 100 iterations, ANXIETY should have occurred at least once", isAnxious);
        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);

    }

    @Test
    public void testNeutralParticipantInNeutralStudy() {

        R34Participant p;
        boolean isNeutral = false;

        for(int i = 0; i< 100; i++)  {
            p = service.create();
            if(p.getCondition().equals(R34Participant.CONDITION.NEUTRAL)) {
                isNeutral = true;
                assertTrue(p.getStudy() instanceof R34NeutralStudy);
            }
        }

        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);
    }


}
