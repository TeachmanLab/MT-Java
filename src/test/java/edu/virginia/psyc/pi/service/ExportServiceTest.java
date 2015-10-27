package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.Questionnaire.AnxietyTriggers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static junit.framework.Assert.*;

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

    @Test
    public void testUpdated() {
        AnxietyTriggers triggers = new AnxietyTriggers();
        triggers.setAnxiousFear(1);
        service.recordUpdated(triggers);

        assertTrue(service.exportNeeded());
    }


    @Test
    public void testAppendToFile() throws IOException {

        String textFile = "/home/dan/code/pi/PIServer/wd/writeOnly.txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(textFile, false));

        bw.write("This is an extra line, to a file that is write only, in a directory that is write only.");
        bw.flush();
        bw.close();

    }

    /**
     * The following ssh command would allow us to append to a remotefile
     * ssh user@remoteserver "/sbin/cat >>remotefile" <localfile
     */

}
