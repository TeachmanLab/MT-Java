package org.mindtrails.service;

import org.mindtrails.Application;
import org.mindtrails.MockClasses.TestQuestionnaire;
import org.mindtrails.MockClasses.TestStudy;
import org.mindtrails.MockClasses.TestUndeleteable;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.ExportLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class ExportServiceTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ExportServiceTest.class);

    @Autowired
    private ExportService service;

    @Autowired
    private EntityManager entityManager;

    private void createTestEntries() {
        Participant p = new Participant("Joe Test", "j@t.com", false);
        p.setStudy(new TestStudy());
        entityManager.persist(p);
        entityManager.persist(new TestQuestionnaire("MyTestValue",p));
        entityManager.persist(new TestUndeleteable("MyTestValue"));
        entityManager.persist(new GiftLog(p, "Session 1", 500));
        entityManager.persist(new EmailLog(p,"Email1"));
    }

    @Test
    public void testExportableEntityIncluded() {
        createTestEntries();
        List<Scale> infos = service.listRepositories();
        Scale tests = null;
        Scale testUndeleteables = null;
        Scale giftLog = null;
        Scale emailLog = null;

        for(Scale i : infos) {
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
