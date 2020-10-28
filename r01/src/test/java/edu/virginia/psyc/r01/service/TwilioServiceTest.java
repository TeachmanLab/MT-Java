package edu.virginia.psyc.r01.service;

import edu.virginia.psyc.r01.Application;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.ScheduledEvent;
import org.mindtrails.persistence.ParticipantRepository;
import org.mindtrails.service.ScheduledEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TwilioServiceTest {

    @Autowired
    private ScheduledEventService scheduledEventService;

    @Autowired
    private R01ParticipantService participantService;

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant participant;

    @Before
    public void setup() {
        // Create a participant
        participant = participantService.create();
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setLastLoginDate(xDaysAgo(7));
        participant.setEmailReminders(false);
        participant.setPhoneReminders(true);
        participantRepository.save(participant);
    }

    private Date xDaysAgo(int i) {
        return new DateTime().minus(Period.days(i)).toDate();
    }


    @Test
    public void noMessageIfInactive() throws Exception {
        participant.setEmail("noMessageIfInactive");
        participant.getStudy().forceToSession("secondSession");
        participantRepository.save(participant);
        assertEquals(1, scheduledEventService.getEventsForParticipant(participant).size());
        participant.setActive(false);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
    }

    @Test
    public void specificMessageFor18Days() throws Exception {
        participant.setEmail("specificMessageFor18Days");
        participantRepository.save(participant);
        participant.setLastLoginDate(xDaysAgo(21));
        participant.getStudy().forceToSession("secondSession");
        List<ScheduledEvent> events = scheduledEventService.getEventsForParticipant(participant);
        assert(events.get(0).content().startsWith("MindTrails account closed."));
    }

    @Test
    public void noMessageOnOffDays() throws Exception {
        participant.setEmail("noMessageOnOffDays");
        participant.setLastLoginDate(xDaysAgo(1));
        participantRepository.save(participant);
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        participant.setLastLoginDate(xDaysAgo(8));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        participant.setLastLoginDate(xDaysAgo(30));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        participant.setLastLoginDate(xDaysAgo(60));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
        participant.setLastLoginDate(xDaysAgo(60));
        assertEquals(0, scheduledEventService.getEventsForParticipant(participant).size());
    }


}
