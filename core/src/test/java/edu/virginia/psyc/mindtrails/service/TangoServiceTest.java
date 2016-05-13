package edu.virginia.psyc.mindtrails.service;

import edu.virginia.psyc.mindtrails.Application;
import edu.virginia.psyc.mindtrails.domain.Participant;
import edu.virginia.psyc.mindtrails.domain.tango.Account;
import edu.virginia.psyc.mindtrails.domain.tango.Order;
import edu.virginia.psyc.mindtrails.domain.tango.Reward;
import edu.virginia.psyc.mindtrails.persistence.ParticipantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class TangoServiceTest {

    @Autowired
    private TangoService service;

    @Autowired
    private ParticipantRepository participantRepository;

    private Participant participant;

    @Before
    public void setup() {
        // Create a participant
        participant = new Participant("Dan", "j.q.tester@gmail.com", true);
    }

    @Test
    public void testGetAccountInformation() {
        Account account = service.getAccountInfo();
        assertTrue("Test accounts should have some money in it:" + account.toString(), account.getAvailable_balance() > 0);
    }

    @Test
    public void giveParticipantAGift() {
        Reward reward = service.createGiftCard(participant, "TEST_SESSION", 1);
        assertNotNull("A reward is returned.", reward);
        assertNotNull("The reward has a token", reward.getToken());

    }

    @Test
    public void getGiftDetails() {
        // Send a reward
        Reward reward = service.createGiftCard(participant, "TEST_SESSION", 1);

        // Now Get the details of that reward form the API.
        Order order = service.getOrderInfo(reward.getOrder_id());

        assertEquals("Gift card should be sent to pariticipant", participant.getEmail(), order.getRecipient().getEmail());
        assertEquals("Gift award should be $5 (measured in cents)", 1, order.getAmount());

    }

}
