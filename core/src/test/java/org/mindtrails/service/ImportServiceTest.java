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

import java.io.FileFilter;
import java.util.ArrayList;
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
public class ImportServiceTest {

    @Autowired
    private ImportService service;
    private String testScale = "JsPsychTrial";


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
    public void backup() throws Exception {
        LOGGER.info("Successfully launch backup");
        List<String> list = service.getLocal(testScale);
        LOGGER.info("Successfully get data from local: " + list);
        assertTrue(service.localBackup(testScale,list));
    }

    @Test
    public void getScaleLocally() throws Exception {
        LOGGER.info("Successfully launch backup program");
        List<String> list = service.getLocal(testScale);
        for (String item:list) {
        LOGGER.info("Successfully get data from local: " + item);
        }
        assertNotNull(list);
    }

    @Test
    public void getType() throws Exception {
        LOGGER.info("Successfully fired getScaleType");
        Class<?> ans = service.getClass(testScale);
        LOGGER.info("Got it! This is a "+ans +" !");
        assertNotNull(ans);
    }

    @Test
    public void saveScale() throws Exception {
        LOGGER.info("Save scale fired.");
        String is = service.getOnline(testScale);
        LOGGER.info("Got the data! " + is);
        assertTrue(service.parseDatabase(testScale,is));
    }


    @Test
    public void saveAllScale() throws Exception {
        LOGGER.info("Save All Scale!!!!");
        int i = 0;
        List<String> good = new ArrayList<String>();
        List<String> bad = new ArrayList<String>();
        List<Scale> list = service.importList();
        for (Scale scale:list) {
            String is = service.getOnline(scale.getName());
            if (service.parseDatabase(scale.getName(),is)) {
                i+=1;
                good.add(scale.getName());
            } else {
                bad.add(scale.getName());
            }
        }
        LOGGER.info("Here is the good list:");
        for (String flag:good) LOGGER.info(flag);
        LOGGER.info("Here is the bad list:");
        for (String flag:bad) LOGGER.info(flag);
        assertEquals(i,list.size());
    }




}



