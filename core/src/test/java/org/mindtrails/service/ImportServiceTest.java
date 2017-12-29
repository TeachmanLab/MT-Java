package edu.virginia.psych.templeton;


import edu.virginia.psyc.templeton.domain.TempletonStudy;
import org.mindtrails.domain.Participant;
import org.mindtrails.domain.importData.Scale;
import org.mindtrails.domain.tracking.EmailLog;
import org.mindtrails.domain.tracking.GiftLog;
import org.mindtrails.service.ExportService;
import org.mindtrails.service.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TempletonStudy.class)
@ActiveProfiles("default")
public class ImportServiceTest {

    ImportService service = new ImportService();
    String testScale = "Phq4";


    //private EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger((ImportServiceTest.class));

//    private void createTestEntries() {
//        Participant p = new Participant("Joe Test", "j@t.com", false);
//        entityManager.persist(p);
//        entityManager.persist(new TestQuestionnaire("MyTestValue"));
//        entityManager.persist(new TestUndeleteable("MyTestValue"));
//        entityManager.persist(new GiftLog(p, "Order1", "Session 1"));
//        entityManager.persist(new EmailLog(p,"Email1"));
//    }


    @Test
    public void getScaleList() throws Exception {
        LOGGER.info("Check point 1");
        List<Scale> list = service.importList();
        LOGGER.info("Check point 2");
        for (Scale scale : list) {
            LOGGER.info(scale.getName());
        }
        assertNotNull(list);
    }


    @Test
    public void getScale() throws Exception {
        LOGGER.info("Successfully fired getScale()");
        String list = service.getOnline(testScale);
        LOGGER.info("Successfully read online: "+ list);
        assertNotNull(list);
    }

    @Test
    public void getType() throws Exception {
        LOGGER.info("Successfully fired getScaleType");
        Class<?> ans = service.getScaleType(testScale);
        LOGGER.info("Got it! This is a "+ans.getName() +" !");
        assertNotNull(ans);
    }





}
