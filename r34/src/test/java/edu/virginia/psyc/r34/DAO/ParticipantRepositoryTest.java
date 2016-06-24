package edu.virginia.psyc.r34.DAO;

import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.CBMStudy;
import edu.virginia.psyc.r34.domain.PiParticipant;
import edu.virginia.psyc.r34.persistence.ParticipantExportDAO;
import edu.virginia.psyc.r34.persistence.ParticipantExportRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
@ActiveProfiles("test")
public class ParticipantRepositoryTest {


    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected ParticipantExportRepository exportRepository;

    @Test
    public void bugWhereParticipantSessionIsWonky() {

        PiParticipant p;
        Participant pSaved;
        Study study;
        String email = "john@x.com";

        // Create a participant
        p = new PiParticipant("John", email, false);
        study = new CBMStudy(CBMStudy.NAME.PRE.toString(), 0, new Date(), new ArrayList<>(), true);
        p.setStudy(study);

        // Save that participant
        participantRepository.save(p);
        participantRepository.flush();
        pSaved = participantRepository.findOne(p.getId());

        // Assure that the participant's current session is pre
        assertEquals(CBMStudy.NAME.PRE.toString(), pSaved.getStudy().getCurrentSession().getName());

        // Change the participant's session.
        pSaved.getStudy().forceToSession(CBMStudy.NAME.SESSION5.toString());

        // Get that participant back.
        participantRepository.save(pSaved);
        participantRepository.flush();
        pSaved = participantRepository.getOne(pSaved.getId());

        // Assure that the participant's current session is set to 5
        assertEquals(CBMStudy.NAME.SESSION5.toString(), pSaved.getStudy().getCurrentSession().getName());

        // Increment the current task.
        p.getStudy().completeCurrentTask();
        assertEquals(1, p.getStudy().getCurrentTaskIndex());
        p.getStudy().completeCurrentTask();
        p.getStudy().completeCurrentTask();
        p.getStudy().completeCurrentTask();
        assertEquals(0, p.getStudy().getCurrentTaskIndex());
        assertEquals(CBMStudy.NAME.SESSION6.toString(), p.getStudy().getCurrentSession().getName());

        // Change the participant's session back to Session1.
        p.getStudy().forceToSession(CBMStudy.NAME.SESSION1.toString());
        participantRepository.save(p);
        participantRepository.flush();
        pSaved = participantRepository.getOne(pSaved.getId());

        assertEquals(CBMStudy.NAME.SESSION1.toString(), p.getStudy().getCurrentSession().getName());
        p.getStudy().completeCurrentTask();
        assertEquals(CBMStudy.NAME.SESSION1.toString(), p.getStudy().getCurrentSession().getName());

    }

    @Test
    @Transactional
    public void participantCanBeRetrievedAsExportableObject() {

        Participant participant;
        ParticipantExportDAO exportDAO = null;
        List<ParticipantExportDAO> exportList;

        // Create a participant
        participant = new PiParticipant("John Export", "john@x.com",  false);
        participant.setTheme("green");
        participantRepository.save(participant);
        participantRepository.flush();

        Assert.assertNotNull(participant.getId());
        Assert.assertNotEquals(0, participant.getId());

        exportList = exportRepository.findAll();

        for (ParticipantExportDAO e : exportList) {
            if (e.getId() == participant.getId()) {
                exportDAO = e;
            }
        }

        assertNotNull(exportDAO);
        assertEquals("green", exportDAO.getTheme());
        assertEquals(false, exportDAO.isAdmin());
    }
}