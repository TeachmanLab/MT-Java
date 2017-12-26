package edu.virginia.psych.templeton;

import edu.virginia.psyc.templeton.domain.TempletonStudy;
import org.mindtrails.service.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;

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

    private static final Logger LOGGER = LoggerFactory.getLogger((ImportServiceTest.class));

    @Test
    public void getScaleList() throws Exception {
        LOGGER.info("Check point 1");
        String list = service.importList();
        LOGGER.info("Check point 2");
        LOGGER.info(list);
        assertNotNull(list);
    }

}
