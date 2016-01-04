package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.DAO.TestQuestionnaire;
import edu.virginia.psyc.pi.DAO.TestQuestionnaireRepository;
import edu.virginia.psyc.pi.persistence.ExportLogDAO;
import edu.virginia.psyc.pi.persistence.ExportLogRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.AnxietyTriggers;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    private void createTestEntry() {
        TestQuestionnaire q = new TestQuestionnaire();
        q.setDate(new Date());
        q.setValue("MyTestValue");
        repo.save(q);
        repo.flush();
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
