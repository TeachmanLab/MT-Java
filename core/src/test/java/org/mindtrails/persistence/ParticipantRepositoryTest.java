package org.mindtrails.persistence;

import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.Study;
import org.mindtrails.domain.tango.Item;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Assure that Participants are correctly stored and retrieved from the database.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ParticipantRepositoryTest {


    @Autowired
    protected ParticipantRepository participantRepository;

    @Test
    public void testPasswordIsEncryptedWhenStored() {
        Participant p;
        String password = "Abcdefg1#";

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();

        p = new Participant("Dan Funk", "daniel.h.funk@gmail.com", false);
        p.updatePassword(password);

        Assert.assertNotNull(p.getPassword());
        Assert.assertNotSame(password, p.getPassword());
        Assert.assertTrue(encoder.matches(password, p.getPassword()));

        // If the password isn't set, it doesn't overwrite the DAO.
        p.updatePassword(null);
        Assert.assertNotNull(p.getPassword());
        Assert.assertNotSame(password, p.getPassword());
        Assert.assertTrue(encoder.matches(password, p.getPassword()));

    }


    @Test
    @Transactional
    public void savedEmailLogSurfacesInReturnedParticipant() {

        Participant participant;
        EmailLog log;
        EmailLog log2;

        // Create a participant
        participant = new Participant("John", "john@x.com", false);
        participant.setStudy(new TestStudy());
        log = new EmailLog(participant, "day2");
        participant.addEmailLog(log);
        participantRepository.save(participant);
        participantRepository.flush();

        // Participant should have an id now that it is saved.
        Assert.assertNotNull(participant.getId());

        // Verify that the log message is returned, when loading that participant back up.
        participant = participantRepository.findOne(participant.getId());

        Assert.assertNotNull(participant);
        Assert.assertNotNull(participant.getEmailLogs());
        Assert.assertEquals(1, participant.getEmailLogs().size());
        log = participant.getEmailLogs().iterator().next();
        Assert.assertEquals("day2", log.getEmailType());
        Assert.assertNotNull(log.getDateSent());

    }

    @Test
    @Transactional
    public void giftLogSurfacesInReturnedParticipant() {
        Participant participant;
        GiftLog logDao;
        GiftLog log;

        participant = new Participant("Dan", "Smith@x.com", false);
        Item item = new Item();
        item.setCurrencyCode("USD");
        item.setUtid("XXX111222");
        logDao = new GiftLog(participant, "SESSION1", 5, 5, item);
        logDao.setOrderId("code123");
        logDao.setDateSent(new Date());
        participant.addGiftLog(logDao);
        participantRepository.save(participant);
        participantRepository.flush();

        Assert.assertNotNull(participant.getId());

        Assert.assertNotNull(participant);
        Assert.assertNotNull(participant.getGiftLogs());
        Assert.assertEquals(1, participant.getGiftLogs().size());
        logDao = participant.getGiftLogs().iterator().next();
        Assert.assertEquals("code123", logDao.getOrderId());
        Assert.assertNotNull(logDao.getDateSent());

    }

    @Test
    @Transactional
    public void testFindByToken() {
        Participant participant;
        Participant p, p2;
        PasswordToken token;
        String tokenString = "abcfedf";

        // Create a participant
        participant = new Participant("John", "john@x.com", false);

        // Create a token DAO object
        token = new PasswordToken(participant, new Date(), tokenString);

        // Connect the two and save.
        participant.setPasswordToken(token);
        participantRepository.save(participant);
        participantRepository.flush();

        // Find the participant by the token.
        participant = participantRepository.findByToken(tokenString);

        Assert.assertNotNull(participant);
        Assert.assertEquals(tokenString, participant.getPasswordToken().getToken());
        Assert.assertEquals("john@x.com", participant.getEmail());
    }

    @Test
    @Transactional
    public void testFindByCondition() {
        Participant p, p2, p3;
        TestStudy s, s2, s3;

        // Create a few participants
        p = new Participant("John", "john@x.com", false);
        s = new TestStudy();
        s.setConditioning("unassigned");
        p.setStudy(s);

        // Create a few participants
        p2 = new Participant("James", "hames@x.com", false);
        s2 = new TestStudy();
        s2.setConditioning("control");
        p2.setStudy(s2);

        // Create a few participants
        p3 = new Participant("Mark", "marks@x.com", false);
        s3 = new TestStudy();
        s3.setConditioning("unassigned");
        p3.setStudy(s3);


        participantRepository.save(p);
        participantRepository.save(p2);
        participantRepository.save(p3);
        participantRepository.flush();

        // Find the participant by the token.
        List<Participant> all = participantRepository.findAllByCondition("unassigned");

        Assert.assertNotNull(all);
        Assert.assertEquals(2, all.size());
        Assert.assertEquals("unassigned", all.get(0).getStudy().getConditioning());
        Assert.assertEquals("unassigned", all.get(1).getStudy().getConditioning());
    }

    @Test
    @Transactional
    public void testFindCoachesAndCoachees() {

        // Create a participant
        Participant coach = new Participant("John", "john@x.com", false);
        coach.setCoaching(true);

        Participant p1 = new Participant("Paul", "paul@beatles.com", false);
        Participant p2 = new Participant("George", "george@beatles.com", false);
        Participant p3 = new Participant("Ringo", "ringo@beatles.com", false);

        coach.setCoaching(true);
        p1.setCoachedBy(coach);
        p2.setCoachedBy(coach);

        participantRepository.save(coach);
        participantRepository.save(p1);
        participantRepository.save(p2);
        participantRepository.save(p3);

        PageRequest pageRequest = new PageRequest(0, 20);
        Page<Participant> coachees =  participantRepository.findByCoachedBy(coach, pageRequest);

        List<Participant> coaches =  participantRepository.findCoaches();


        Assert.assertEquals("John doesn't coach Ringo", 2, coachees.getTotalElements());
        Assert.assertEquals("John is the only coach", 1, coaches.size());
    }

    @Test
    @Transactional
    public void listParticipantsEligible() {

        Study s1 = new TestStudy("SessionOne", 0, "COACH");
        Study s2 = new TestStudy("SessionOne", 0, "NO_COACH");
        Study s3 = new TestStudy("SessionOne", 0, "TRAINING");
        Study s4 = new TestStudy("SessionOne", 0, "CONTROL");
        Study s5 = new TestStudy("SessionOne", 0, "COACH");
        Study s6 = new TestStudy("SessionOne", 0, "COACH");


        Participant p1 = new Participant("Paul", "paul@beatles.com", false);
        Participant p2 = new Participant("George", "george@beatles.com", false);
        Participant p3 = new Participant("Ringo", "ringo@beatles.com", false);
        Participant p4 = new Participant("John", "john@beatles.com", false);
        Participant p5 = new Participant("Scott", "scott@beatles.com", false);
        Participant p6 = new Participant("Francis", "francis@beatles.com", false);

        p1.setStudy(s1);
        p2.setStudy(s2);
        p3.setStudy(s3);
        p4.setStudy(s4);
        p5.setStudy(s5);
        p6.setStudy(s6);

        p1.setAttritionRisk(.5f);
        p2.setAttritionRisk(.2f);
        p3.setAttritionRisk(.7f);
        p5.setCoaching(true);
        p6.setTestAccount(true);

        participantRepository.save(p1);
        participantRepository.save(p2);
        participantRepository.save(p3);
        participantRepository.save(p4);
        participantRepository.save(p5);
        participantRepository.save(p6);

        PageRequest pageRequest = new PageRequest(0, 20);
        Page<Participant> coachees =  participantRepository.findEligibleForCoaching("COACH", pageRequest);

        // Should be two, because three are three participants in the COACH condition, all have no coach assigned, but
        // one of them is a test account.
        Assert.assertEquals(2, coachees.getTotalElements() );
    }

}