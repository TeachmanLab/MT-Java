package org.mindtrails.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TwilioServiceTest {

    @Autowired
    private TwilioService service;

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant participant;

    @Before
    public void setup() {
        // Create a participant
        participant = new Participant("Dan", "j.q.tester@gmail.com", true);
        participant.setEmail("tester@test.com");
        participant.setFullName("Tester McTest");
        participant.setStudy(new TestStudy());
        participant.setLastLoginDate(xDaysAgo(2));
    }

    private Date xDaysAgo(int i) {
        return new DateTime().minus(Period.days(i)).toDate();
    }


    @Test
    public void noMessageIfInactive() throws Exception {
        assertFalse("No message, but should be there", service.getMessage(participant).isEmpty());
        participant.setActive(false);
        assertTrue("No messages for an inactive participant.", service.getMessage(participant).isEmpty());
    }

    @Test
    public void specificMessageFor18Days() throws Exception {
        participant.setLastLoginDate(xDaysAgo(18));
        assert(service.getMessage(participant).startsWith("MindTrails account closed."));
    }

    @Test
    public void noMessageIfOnlyOneDay() throws Exception {
        participant.setLastLoginDate(xDaysAgo(1));
        assert(service.getMessage(participant).isEmpty());
    }

    @Test
    public void noMessageOnThirdDay() throws Exception {
        participant.setLastLoginDate(xDaysAgo(3));
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
        participant.setLastLoginDate(xDaysAgo(60));
        participant.setStudy(new TestStudy("PostSession",0));
        //participant.getStudy().getCurrentSessionModel().setDaysToWait(60);
        assertFalse(service.getMessage(participant).isEmpty());
    }

    @Test
    public void ifTimeIsCloseItIsTimeToSend() throws Exception {

        // Set the time of day to notify to be right now.
        service.setNotifyHour(DateTime.now().getHourOfDay());
        service.setNotifyMinute(DateTime.now().getMinuteOfHour());


        assert(service.timeToSendMessage(participant));

        // But not if their timezone is different.
        participant.setTimezone("America/Anchorage");
        assertFalse(service.timeToSendMessage(participant));

        // But this timezone should be all good.



        participant.setTimezone(TimeZone.getDefault().getID());
        assert(service.timeToSendMessage(participant));


    }

}
