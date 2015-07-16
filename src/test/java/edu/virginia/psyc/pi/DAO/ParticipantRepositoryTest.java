package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.domain.EmailLog;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.PasswordToken;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.*;
import edu.virginia.psyc.pi.service.EmailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

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
@SpringApplicationConfiguration(classes = Application.class)
public class ParticipantRepositoryTest {


    @Autowired
    protected ParticipantRepository participantRepository;


    @Test
    public void testEntityToDomain() {

        ParticipantDAO dao;
        Participant p;
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();

        dao = new ParticipantDAO("Dan Funk", "dan@sartography.com", "password", false);
        dao.setCurrentSession(Session.NAME.SESSION1);
        dao.setTaskIndex(1);
        dao.setEmailOptout(true);
        dao.setActive(false);
        dao.setLastLoginDate(new Date());
        dao.setLastSessionDate(new Date());
        dao.setPasswordTokenDAO(new PasswordTokenDAO(dao, new Date(), "1234"));

        p = repository.entityToDomain(dao);

        assertEquals(p.getId(), dao.getId());
        assertEquals(p.getFullName(), dao.getFullName());
        assertEquals(p.getEmail(), dao.getEmail());
        assertEquals(p.isAdmin(), dao.isAdmin());
        assertEquals(Session.NAME.SESSION1, p.getCurrentSession().getName());
        assertEquals(p.getCurrentSession().getName(), dao.getCurrentSession());
        assertEquals(p.isEmailOptout(), dao.isEmailOptout());
        assertEquals(p.isActive(), dao.isActive());
        assertEquals(1, p.getTaskIndex());
        assertNotNull(p.getCurrentSession());
        assertEquals(p.getLastLoginDate(), dao.getLastLoginDate());
        assertEquals(p.getLastSessionDate(), dao.getLastSessionDate());
        assertNull("Password should not come back from the database.", p.getPassword());
        assertEquals(p.getCbmCondition(), dao.getCbmCondition());
        assertEquals(p.getPrime(), dao.getPrime());
    }

    @Test
    public void testPasswordTokenIsTransferedToDomain() {
        ParticipantDAO dao;
        Participant p;
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();

        dao = new ParticipantDAO("Dan Funk", "dan@sartography.com", "password", false);
        dao.setPasswordTokenDAO(new PasswordTokenDAO(dao, new Date(), "1234"));

        p = repository.entityToDomain(dao);

        assertNotNull(p.getPasswordToken());
        assertEquals("1234", p.getPasswordToken().getToken());
        assertTrue("Newly created token is considered valid", p.getPasswordToken().valid());
    }

    @Test
    public void testPasswordTokenIsTransferedToEntity() {
        Participant p;
        ParticipantDAO dao = new ParticipantDAO();
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setPasswordToken(new PasswordToken());

        repository.domainToEntity(p, dao);

        assertNotNull(dao.getPasswordTokenDAO());
        assertNotNull(dao.getPasswordTokenDAO().getToken());
        assertEquals(20, dao.getPasswordTokenDAO().getToken().length());
    }

    @Test
    public void testPasswordIsEncryptedWhenStored() {
        Participant p;
        ParticipantDAO dao = new ParticipantDAO();
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();
        String password = "Abcdefg1#";

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setPassword(password);

        repository.domainToEntity(p, dao);

        Assert.assertNotNull(dao.getPassword());
        Assert.assertNotSame(password, dao.getPassword());
        Assert.assertTrue(encoder.matches(password, dao.getPassword()));

        // If the password isn't set, it doesn't overwrite the DAO.
        p.setPassword(null);
        repository.domainToEntity(p, dao);

        Assert.assertNotNull(dao.getPassword());
        Assert.assertNotSame(password, dao.getPassword());
        Assert.assertTrue(encoder.matches(password, dao.getPassword()));

    }

    @Test
    public void testDomainToEntity() {
        Participant p;
        ParticipantDAO dao = new ParticipantDAO();
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();
        List<Session> sessions = Session.createListView(Session.NAME.SESSION1, 1);

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setSessions(sessions);
        p.setLastLoginDate(new Date());
        p.setLastSessionDate(new Date());

        repository.domainToEntity(p, dao);

        assertEquals(p.getId(), dao.getId());
        assertEquals(p.getFullName(), dao.getFullName());
        assertEquals(p.getEmail(), dao.getEmail());
        assertEquals(p.isAdmin(), dao.isAdmin());
        assertEquals(p.getCurrentSession().getName(), dao.getCurrentSession());
        assertEquals(p.isActive(), dao.isActive());
        assertEquals(1, dao.getTaskIndex());
        assertEquals(p.getLastLoginDate(), dao.getLastLoginDate());
        assertEquals(p.getLastSessionDate(), dao.getLastSessionDate());
        assertEquals(p.getCbmCondition(), dao.getCbmCondition());
        assertEquals(p.getPrime(), dao.getPrime());

    }

    @Test
    @Transactional
    public void savedEmailLogSurfacesInReturnedParticipant() {

        ParticipantDAO participantDAO;
        Participant p;
        EmailLogDAO log;
        EmailLog log2;

        // Create a participant
        participantDAO = new ParticipantDAO("John", "john@x.com", "12341234", false);
        log            = new EmailLogDAO(participantDAO, EmailService.TYPE.day2);
        participantDAO.addLog(log);
        participantRepository.save(participantDAO);
        participantRepository.flush();

        // Participant should have an id now that it is saved.
        Assert.assertNotNull(participantDAO.getId());

        // Verify that the log message is returned, when loading that participant back up.
        participantDAO = participantRepository.findOne(participantDAO.getId());

        Assert.assertNotNull(participantDAO);
        Assert.assertNotNull(participantDAO.getEmailLogDAOs());
        Assert.assertEquals(1, participantDAO.getEmailLogDAOs().size());
        log = participantDAO.getEmailLogDAOs().iterator().next();
        Assert.assertEquals(EmailService.TYPE.day2, log.getEmailType());
        Assert.assertNotNull(log.getDateSent());

        p = participantRepository.entityToDomain(participantDAO);

        Assert.assertNotNull(p.getEmailLogs());
        Assert.assertEquals(1, p.getEmailLogs().size());

        log2 = p.getEmailLogs().get(0);
        Assert.assertEquals(EmailService.TYPE.day2, log2.getType());
        Assert.assertNotNull(log2.getDate());

    }

    @Test
    @Transactional
    public void testFindByToken() {
        ParticipantDAO participantDAO;
        Participant p, p2;
        PasswordTokenDAO token;
        String tokenString = "abcfedf";

        // Create a participant
        participantDAO = new ParticipantDAO("John", "john@x.com", "12341234", false);

        // Create a token DAO object
        token = new PasswordTokenDAO(participantDAO, new Date(), tokenString);

        // Connect the two and save.
        participantDAO.setPasswordTokenDAO(token);
        participantRepository.save(participantDAO);
        participantRepository.flush();

        // Find the participant by the token.
        participantDAO = participantRepository.findByToken(tokenString);

        Assert.assertNotNull(participantDAO);
        Assert.assertEquals(tokenString, participantDAO.getPasswordTokenDAO().getToken());
        Assert.assertEquals("john@x.com", participantDAO.getEmail());
    }


    @Test
    public void bugWhereParticipantSessionIsWonky() {

        ParticipantDAO dao;
        Participant p;
        String email = "john@x.com";

        // Create a participant
        p = new Participant(1000000, email, "23452354", false);
        p.setSessions(Session.createListView(Session.NAME.PRE,0));

        // Save that participant
        dao = new ParticipantDAO();
        participantRepository.domainToEntity(p, dao);

        // Get that participant back.
        p   = participantRepository.entityToDomain(dao);

        // Assure that the participant's current session is pre
        assertEquals(Session.NAME.PRE, p.getCurrentSession().getName());

        // Change the participant's session.
        dao.setCurrentSession(Session.NAME.SESSION5);

        // Get that participant back.
        p   = participantRepository.entityToDomain(dao);

        // Assure that the participant's current session is session 5
        assertEquals(Session.NAME.SESSION5, p.getCurrentSession().getName());

        // Increment the current task.
        p.completeCurrentTask();
        assertEquals(1, p.getTaskIndex());
        p.completeCurrentTask();
        p.completeCurrentTask();
        p.completeCurrentTask();
        assertEquals(0, p.getTaskIndex());
        assertEquals(Session.NAME.SESSION6, p.getCurrentSession().getName());

        // Change the participant's session back to Session1.
        dao.setCurrentSession(Session.NAME.SESSION1);
        p   = participantRepository.entityToDomain(dao);
        assertEquals(Session.NAME.SESSION1, p.getCurrentSession().getName());
        p.completeCurrentTask();
        assertEquals(Session.NAME.SESSION1, p.getCurrentSession().getName());

    }


}