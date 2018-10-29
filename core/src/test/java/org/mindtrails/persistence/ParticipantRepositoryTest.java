package org.mindtrails.persistence;

import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.PasswordToken;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        logDao = new GiftLog(participant, "code123", "SESSION1");
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

        List<Participant> coachees =  participantRepository.findByCoachedBy(coach);

        List<Participant> coaches =  participantRepository.findByCoaching(true);


        Assert.assertEquals("John doesn't coach Ringo", 2, coachees.size());
        Assert.assertEquals("John is the only coach", 1, coaches.size());


    }

}