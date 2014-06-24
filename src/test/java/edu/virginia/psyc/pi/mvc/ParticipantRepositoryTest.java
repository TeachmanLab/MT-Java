package edu.virginia.psyc.pi.mvc;

import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import edu.virginia.psyc.pi.persistence.ParticipantRepositoryImpl;
import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
        p = repository.entityToDomain(dao);

        assertEquals(p.getId(), dao.getId());
        assertEquals(p.getFullName(), dao.getFullName());
        assertEquals(p.getEmail(), dao.getEmail());
        assertEquals(p.isAdmin(), dao.isAdmin());
        assertEquals(p.getCurrentSession(), dao.getCurrentSession());
        assertNotNull(p.getCurrentSession());

    }

}