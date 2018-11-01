package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.Application;
import edu.virginia.psyc.r01.domain.R01Study;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class R01ParticipantServiceTest {

    @Autowired
    R01ParticipantService service;


    @Test
    public void testNewParticipantGetsRandomCBMCondition() {

        Participant participant;
        R01Study study;

        boolean isComputer = false;
        boolean isPhone = false;
        boolean isNeutral = false;
        boolean isUnexpected = false;
        String  unexpected = "";

        participant = service.create();
        assertTrue(participant.getStudy() instanceof R01Study);
        study = (R01Study) participant.getStudy();
        assertNotNull(study.getConditioning());

        for(int i = 0; i< 100; i++)  {
            participant = service.create();
            study = (R01Study) participant.getStudy();
            if(study.getConditioning().equals("COMPUTER")) isComputer = true;
            else if(study.getConditioning().equals("MOBILE")) isPhone = true;
            else if(study.getConditioning().equals("NEUTRAL")) isNeutral = true;
            else {
                isUnexpected = true;
                unexpected = study.getConditioning().toString();
            }
        }

        assertTrue("after 100 iterations, computer should have occurred at least once", isComputer);
        assertTrue("after 100 iterations, mobile should have occurred at least once", isPhone);
        assertTrue("after 100 iterations, Neutral should have occurred at least once", isNeutral);
        assertFalse("an unexpected condition with value " + unexpected + " occurred at least once", isUnexpected);

    }


}
