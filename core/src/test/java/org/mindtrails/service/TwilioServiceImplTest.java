package org.mindtrails.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.MockClasses.TestTwilioService;
import org.mindtrails.domain.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TwilioServiceImplTest {

    @Autowired
    private TestTwilioService service;

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
