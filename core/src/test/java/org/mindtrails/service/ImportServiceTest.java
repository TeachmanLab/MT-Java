package org.mindtrails.service;

import org.mindtrails.Application;
import org.mindtrails.domain.importData.Scale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

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
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class ImportServiceTest {

    @Autowired
    private ImportService service;
    private String testScale = "participant";


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
        Class<?> ans = service.getClass(testScale);
        LOGGER.info("Got it! This is a "+ans +" !");
        assertNotNull(ans);
    }





}
