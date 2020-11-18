package edu.virginia.psyc.kaiser.service;

import edu.virginia.psyc.kaiser.Application;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TwilioServiceTest {

    @Autowired
    private KaiserTwilioService service;

    @Autowired
    private KaiserParticipantService participantService;

    private Participant participant;

    @Before
    public void setup() {
        // Create a participant
        participant = participantService.create();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setLastLoginDate(xDaysAgo(7));
    }

    private Date xDaysAgo(int i) {
        return new DateTime().minus(Period.days(i)).toDate();
    }


    @Test
    public void noMessageIfInactive() throws Exception {
        assertTrue("No message, but should be there", service.participantShouldGetMessages(participant));
        participant.setActive(false);
        assertFalse("No messages for an inactive participant.", service.participantShouldGetMessages(participant));
    }

    @Test
    public void specificMessageFor18Days() throws Exception {
        participant.setLastLoginDate(xDaysAgo(21));
        assert(service.getMessage(participant).startsWith("MindTrails account closed."));
    }

    @Test
    public void noMessageIfOnlyOneDay() throws Exception {
        participant.setLastLoginDate(xDaysAgo(1));
        assert(service.getMessage(participant).isEmpty());
    }

    @Test
    public void noMessageOnEightDay() throws Exception {
        participant.setLastLoginDate(xDaysAgo(8));
        assert(service.getMessage(participant).isEmpty());
    }

    @Test
    public void noMessageOnThirtyithDay() throws Exception {
        participant.setLastLoginDate(xDaysAgo(30));
        assert(service.getMessage(participant).isEmpty());
    }

    @Test
    public void noMessageOnSixtythDayIfNotALongWait() throws Exception {
        participant.setLastLoginDate(xDaysAgo(60));
        assert(service.getMessage(participant).isEmpty());
    }

    @Test
    public void MessageOnSixtythDayIfALongDelay() throws Exception {
        Participant p = participantService.create();
        p.setLastLoginDate(xDaysAgo(60));
        assert(service.getMessage(p).isEmpty());
    }



}
