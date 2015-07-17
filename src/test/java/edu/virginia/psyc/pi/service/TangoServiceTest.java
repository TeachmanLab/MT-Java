package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.Account;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
public class TangoServiceTest {

    Participant participant;

    @Autowired
    private TangoService service;

    @Autowired
    private ParticipantRepository participantRepository;


    @Before
    public void setup() {
        // Create a participant and save them to the database.
        participant = new Participant(1,"Dan", "daniel.h.funk@gmail.com", true);
        ParticipantDAO dao = new ParticipantDAO();
        participantRepository.domainToEntity(participant, dao);
        participantRepository.save(dao);
        participantRepository.flush();
    }

    @Test
    public void testGetAccountInformation() {
        Account account = service.getAccountInfo();
        assertTrue("Test accounts should have some money in it:" + account.toString(), account.getAvailable_balance() > 0);
    }

    @Test
    public void giveParticipantAGift() {
        Reward reward = service.createGiftCard(participant);
        assertNotNull("A reward is returned.", reward);
        assertNotNull("The reward has a token", reward.getToken());

        // Make sure the gift is logged in the database.
        Assert.assertTrue(participantRepository.findByEmail(participant.getEmail()).getGiftLogDAOs().size() > 0);
    }

}
