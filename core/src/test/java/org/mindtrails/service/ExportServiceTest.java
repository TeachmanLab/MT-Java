package org.mindtrails.service;

import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestQuestionnaire;
import org.mindtrails.MockClasses.TestUndeleteable;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.questionnaire.QuestionnaireInfo;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.ExportLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
@Transactional
public class ExportServiceTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ExportServiceTest.class);

    @Autowired
    private ExportService service;

    @Autowired
    private EntityManager entityManager;

    private void createTestEntries() {
        Participant p = new Participant("Joe Test", "j@t.com", false);
        entityManager.persist(p);
        entityManager.persist(new TestQuestionnaire("MyTestValue"));
        entityManager.persist(new TestUndeleteable("MyTestValue"));
        entityManager.persist(new GiftLog(p, "Order1", "Session 1"));
        entityManager.persist(new EmailLog(p,"Email1"));
    }

    @Test
    public void testExportableEntityIncluded() {
        createTestEntries();
        List<QuestionnaireInfo> infos = service.listRepositories();
        QuestionnaireInfo tests = null;
        QuestionnaireInfo testUndeleteables = null;
        QuestionnaireInfo giftLog = null;
        QuestionnaireInfo emailLog = null;

        for(QuestionnaireInfo i : infos) {
            if (i.getName().equals(TestQuestionnaire.class.getSimpleName())) tests = i;
            if (i.getName().equals(TestUndeleteable.class.getSimpleName())) testUndeleteables = i;
            if (i.getName().equals(GiftLog.class.getSimpleName())) giftLog = i;
            if (i.getName().equals(EmailLog.class.getSimpleName())) emailLog = i;
        }
        assertNotNull(tests);
        assertNotNull(testUndeleteables);
        assert(tests.isDeleteable());
        assertFalse(testUndeleteables.isDeleteable());
        assertNotNull(giftLog);
        assertNotNull(emailLog);
        assertFalse(giftLog.isDeleteable());
        assertFalse(emailLog.isDeleteable());
    }

    @Test
    public void testEntryExists() {
        createTestEntries();
        List infos =  service.listRepositories();
        assertThat((List<Object>)infos, hasItem(hasProperty("name", is("TestQuestionnaire"))));
    }


    @Test
    public void testMinutesSinceLastExport() {
        entityManager.persist(new ExportLog(10));
        assertTrue(service.minutesSinceLastExport() < 1);

        ExportLog log = new ExportLog(1);
        log.setDate(new DateTime(2015,12,1,1,1).toDate());
        entityManager.persist(log);
        assertTrue(service.minutesSinceLastExport() > 100);
    }


}
