package edu.virginia.psyc.pi.controller;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepositoryImpl;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Assure that Trials are correctly stored and retrieved from the database.
 */
public class ParticipantRepositoryTest {

    @Test
    public void participantShouldKnowTheCurrentSession() {

        ParticipantDAO dao;
        Participant p;
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();

        dao = new ParticipantDAO(1, "Dan Funk", "dan@sartography.com", "password", false);
        dao.setCurrentSession(Session.NAME.SESSION1);
        dao.setTaskIndex(1);

        p = repository.entityToDomain(dao);

        assertEquals(p.getId(), dao.getId());
        assertEquals(p.getFullName(), dao.getFullName());
        assertEquals(p.getEmail(), dao.getEmail());
        assertEquals(p.isAdmin(), dao.isAdmin());
        assertEquals(Session.NAME.SESSION1, p.getCurrentSession().getName());
        assertEquals(p.getCurrentSession().getName(), dao.getCurrentSession());
        assertEquals(1, p.getTaskIndex());
        assertNotNull(p.getCurrentSession());

    }

    @Test
    public void testDomainToEntity() {
        Participant p;
        ParticipantDAO dao = new ParticipantDAO();
        ParticipantRepositoryImpl repository = new ParticipantRepositoryImpl();
        List<Session> sessions = Session.createListView(Session.NAME.SESSION1, 1);

        p = new Participant(1, "Dan Funk", "daniel.h.funk@gmail.com", false);
        p.setSessions(sessions);

        repository.domainToEntity(p, dao);

        assertEquals(p.getId(), dao.getId());
        assertEquals(p.getFullName(), dao.getFullName());
        assertEquals(p.getEmail(), dao.getEmail());
        assertEquals(p.isAdmin(), dao.isAdmin());
        assertEquals(p.getCurrentSession().getName(), dao.getCurrentSession());
        assertEquals(1, dao.getTaskIndex());

    }


}