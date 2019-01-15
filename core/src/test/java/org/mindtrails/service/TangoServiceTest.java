package org.mindtrails.service;

import org.mindtrails.Application;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.Account;
import org.mindtrails.domain.tango.Order;
import org.mindtrails.domain.tango.OrderResponse;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TangoServiceTest {

    @Autowired
    private TangoService service;

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant participant;


    @Test
    public void testGetAccountInformation() {
        Account account = service.getAccountInfo();
        assertTrue("Test accounts should have some money in it:" + account.toString(), account.getCurrentBalance() > 0);
    }

    @Test
    public void giveParticipantAGift() {
        participant = new Participant("Dan", "j.q.t.p.tester@gmail.com", true);
        participantRepository.save(participant);
        GiftLog log = new GiftLog(participant, "TEST_SESSION", 1);
        OrderResponse response  = service.awardGiftCard(log);
        assertNotNull("A reward is returned.", response);
        assertNotNull("The reward has a url", response.getReward());

    }

    @Test
    public void getGiftDetails() {
        participant = new Participant("Dan", "j.a.b.c.tester@gmail.com", true);
        participantRepository.save(participant);
        // Send a reward
        GiftLog log = new GiftLog(participant, "TEST_SESSION", 1);
        OrderResponse reward = service.awardGiftCard(log);

        // Now Get the details of that reward form the API.
        OrderResponse orderResponse = service.getOrderInfo(reward.getReferenceOrderID());

        assertEquals("Gift award should be $5 (measured in cents)", 1, orderResponse.getAmount());

    }

}
