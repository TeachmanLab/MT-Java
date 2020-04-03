package org.mindtrails.service;

import org.mindtrails.Application;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.tango.*;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.persistence.ParticipantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(TangoServiceTest.class);

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
        GiftLog log = service.createGiftLogUnsafe(participant, "TEST SESSION", 1);
        OrderResponse response  = service.awardGiftCard(log);
        assertNotNull("A reward is returned.", response);
        assertNotNull("The reward has a url", response.getReward());

    }

    @Test
    public void getGiftDetails() {
        participant = new Participant("Dan", "j.a.b.c.tester@gmail.com", true);
        participantRepository.save(participant);
        // Send a reward
        GiftLog log = service.createGiftLogUnsafe(participant, "TEST SESSION", 1);
        OrderResponse reward = service.awardGiftCard(log);

        // Now Get the details of that reward form the API.
        OrderResponse orderResponse = service.getOrderInfo(reward.getReferenceOrderID());

        assertEquals("Gift award should be $1 (measured in dollars)", 1.0d, new Double(orderResponse.getAmount()).doubleValue());

    }


    @Test
    public void awardItalianGiftCard() {
        participant = new Participant("Dan", "italy.tester@gmail.com", true);
        participant.setAwardCountryCode("IT");
        participantRepository.save(participant);
        GiftLog log = service.createGiftLogUnsafe(participant, "TEST SESSION", 50);
        OrderResponse response  = service.awardGiftCard(log);
        assertNotNull("A reward is returned.", response);
        assertNotNull("The reward has a url", response.getReward());
        System.out.println(response.getReward());

    }

    @Test
    public void awardAllPossibleCountryGiftCards() {
        List<String> countries = Arrays.asList("US", "AR", "AU", "BR", "CA", "DE", "IN", "IE", "IT",
                "NL", "SG", "ES", "GB");
        participant = new Participant("Dan", "all_places.tester@gmail.com", true);
        participantRepository.save(participant);
        for(String country: countries) {
            System.out.println("Running Country:" + country);
            participant.setAwardCountryCode(country);
            GiftLog log = service.createGiftLogUnsafe(participant, "TEST SESSION", 5);
            OrderResponse response  = service.awardGiftCard(log);
            assertNotNull("A reward is returned.", response);
            assertNotNull("The reward has a url", response.getReward());
            assertTrue("Country " + country + " was " + response.getUsAmount(), response.getUsAmount() < 6);  // Should always be less than 6 american dollars.


            log = service.createGiftLogUnsafe(participant, "TEST SESSION", 10);
            response  = service.awardGiftCard(log);
            assertNotNull("A reward is returned.", response);
            assertNotNull("The reward has a url", response.getReward());
            assertTrue("Country " + country + " was " + response.getUsAmount(), response.getUsAmount() > 7);  // Should always be more than 7 american dollars.
            assertTrue("Country " + country + " was " + response.getUsAmount(), response.getUsAmount() < 11);  // Should always be less than 11 american dollars.
            LOGGER.info("Awarded in " + country + " " + log.getAmount() + log.getCurrency());
        }
    }



    @Test
    public void defaultToUSGiftCardsWhenNull() {
        participant = new Participant("Dan", "null.tester@gmail.com", true);
        participant.setAwardCountryCode(null);
        participantRepository.save(participant);
        GiftLog log = service.createGiftLogUnsafe(participant, "TEST SESSION", 50);
        OrderResponse response  = service.awardGiftCard(log);
        assertNotNull("A reward is returned.", response);
        assertNotNull("The reward has a url", response.getReward());
        System.out.println(response.getReward());
    }





    @Test
    public void listCatalog() {
        Catalog catalog = service.getCatalog();
        //System.out.println(catalog.toString());
        List<Item> items = catalog.getItems();
        System.out.println(items);
    }


    @Test
    public void listExchangeRates() {
        System.out.println(service.getExchangeRates());
    }
}
