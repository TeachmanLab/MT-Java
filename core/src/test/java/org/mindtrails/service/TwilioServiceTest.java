package org.mindtrails.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.Application;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Account;
import org.mindtrails.domain.tango.Order;
import org.mindtrails.domain.tango.Reward;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.*;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
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
    }

    @Test
    public void sendTextMessage() throws Exception {

        // Send a message
        service.sendMessage();

    }

}
