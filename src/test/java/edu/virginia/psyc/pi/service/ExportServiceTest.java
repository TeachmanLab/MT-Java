package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.DAO.TestQuestionnaire;
import edu.virginia.psyc.pi.DAO.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.QuestionnaireInfo;
import edu.virginia.psyc.pi.persistence.*;
import edu.virginia.psyc.pi.persistence.Questionnaire.AnxietyTriggers;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ExportServiceTest {


    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ExportServiceTest.class);

    @Autowired
    private ExportService service;

    @Autowired
    private TestQuestionnaireRepository repo;

    @Autowired
    ExportLogRepository exportLogRepository;

    @Autowired
    ParticipantRepository participantRepository;


    private void createTestEntry() {
        TestQuestionnaire q = new TestQuestionnaire();
        q.setDate(new Date());
        q.setValue("MyTestValue");
        repo.save(q);
        repo.flush();
    }

    @Test
    public void testExportableEntityIncluded() {
        List<QuestionnaireInfo> infos = service.listRepositories();
        QuestionnaireInfo participants = null;
        QuestionnaireInfo trials = null;
        QuestionnaireInfo anxietyTriggers = null;
        QuestionnaireInfo giftLog = null;
        QuestionnaireInfo emailLog = null;

        for(QuestionnaireInfo i : infos) {
            if (i.getName().equals(ParticipantExportDAO.class.getSimpleName())) participants = i;
            if (i.getName().equals(TrialDAO.class.getSimpleName())) trials = i;
            if (i.getName().equals(AnxietyTriggers.class.getSimpleName())) anxietyTriggers = i;
            if (i.getName().equals(GiftLogDAO.class.getSimpleName())) giftLog = i;
            if (i.getName().equals(EmailLogDAO.class.getSimpleName())) emailLog = i;
        }
        assertNotNull(participants);
        assertNotNull(trials);
        assertNotNull(anxietyTriggers);
        assert(trials.isDeleteable());
        assertFalse(participants.isDeleteable());
        assertNotNull(giftLog);
        assertNotNull(emailLog);
        assertFalse(giftLog.isDeleteable());
        assertFalse(emailLog.isDeleteable());
    }

    @Test
    public void testEntryExists() {
        createTestEntry();
        List infos =  service.listRepositories();
        assertThat((List<Object>)infos, hasItem(hasProperty("name", is("TestQuestionnaire"))));
    }


    @Test
    public void testMinutesSinceLastExport() {
        exportLogRepository.save(new ExportLogDAO(10));
        assertTrue(service.minutesSinceLastExport() < 1);

        ExportLogDAO log = new ExportLogDAO(1);
        log.setDate(new DateTime(2015,12,1,1,1).toDate());
        exportLogRepository.save(log);
        assertTrue(service.minutesSinceLastExport() > 100);
    }


}
