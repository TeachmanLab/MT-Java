package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.domain.EmailLog;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.EmailLogDAO;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.ParticipantRepositoryImpl;
import edu.virginia.psyc.pi.service.EmailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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


}