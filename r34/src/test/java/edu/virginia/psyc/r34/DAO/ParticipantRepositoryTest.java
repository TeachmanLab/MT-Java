package edu.virginia.psyc.r34.DAO;

import edu.virginia.psyc.r34.Application;
import edu.virginia.psyc.r34.domain.R34Study;
import edu.virginia.psyc.r34.persistence.ParticipantExportDAO;
import edu.virginia.psyc.r34.persistence.ParticipantExportRepository;
import edu.virginia.psyc.r34.service.R34ParticipantService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.Study;
import org.mindtrails.persistence.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ParticipantRepositoryTest {



    @Autowired
    protected R34ParticipantService participantService;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected ParticipantExportRepository exportRepository;

    @Test
    public void bugWhereParticipantSessionIsWonky() {

        Participant p;
        Participant pSaved;
        Study study;
        String email = "john@x.com";

        // Create a participant
        p = participantService.create();
        p.setEmail(email);
        p.setFullName("John");
        study = new R34Study(R34Study.NAME.PRE.toString(), 0, new Date(), new ArrayList<>(), true);
        p.setStudy(study);

        // Save that participant
        participantService.save(p);
        participantService.flush();
        pSaved = participantRepository.findByEmail(email);

        // Assure that the participant's current session is pre
        assertEquals(R34Study.NAME.PRE.toString(), pSaved.getStudy().getCurrentSession().getName());

        // Change the participant's session.
        pSaved.getStudy().forceToSession(R34Study.NAME.SESSION5.toString());

        // Get that participant back.
        participantService.save(pSaved);
        participantService.flush();
        pSaved = participantRepository.findByEmail(email);

        // Assure that the participant's current session is set to 5
        assertEquals(R34Study.NAME.SESSION5.toString(), pSaved.getStudy().getCurrentSession().getName());

        // Increment the current task.
        p.getStudy().setLastSessionDate(DateTime.now().minus(Days.days(3)).toDate());
        p.getStudy().completeCurrentTask(0);
        assertEquals(1, p.getStudy().getCurrentTaskIndex());
        p.getStudy().completeCurrentTask(0);
        p.getStudy().completeCurrentTask(0);
        p.getStudy().completeCurrentTask(0);
        assertEquals(4, p.getStudy().getCurrentTaskIndex());
        p.getStudy().completeCurrentTask(0);
        assertEquals(R34Study.NAME.SESSION6.toString(), p.getStudy().getCurrentSession().getName());

        // Change the participant's session back to Session1.
        p.getStudy().forceToSession(R34Study.NAME.SESSION1.toString());
        participantService.save(p);
        participantService.flush();
        pSaved = participantRepository.findByEmail(email);

        assertEquals(R34Study.NAME.SESSION1.toString(), pSaved.getStudy().getCurrentSession().getName());
        pSaved.getStudy().completeCurrentTask(0);
        assertEquals(R34Study.NAME.SESSION1.toString(), pSaved.getStudy().getCurrentSession().getName());

    }

    @Test
    @Transactional
    public void participantCanBeRetrievedAsExportableObject() {

        Participant participant;
        ParticipantExportDAO exportDAO = null;
        List<ParticipantExportDAO> exportList;
        String email =  "john@x.com";

        // Create a participant
        participant = participantService.create();
        participant.setEmail(email);
        participant.setTheme("green");
        participantService.save(participant);
        participantService.flush();

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