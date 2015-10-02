package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.domain.Participant;
import edu.virginia.psyc.pi.domain.tango.Account;
import edu.virginia.psyc.pi.domain.tango.Order;
import edu.virginia.psyc.pi.domain.tango.Reward;
import edu.virginia.psyc.pi.persistence.ParticipantRepository;
import edu.virginia.psyc.pi.persistence.Questionnaire.AnxietyTriggers;
import edu.virginia.psyc.pi.persistence.Questionnaire.AnxietyTriggersRepository;
import edu.virginia.psyc.pi.persistence.SensitiveData;
import junit.framework.Assert;
import org.apache.commons.math3.analysis.function.Exp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;

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
    public void testUpdated() throws InterruptedException {
        AnxietyTriggers triggers = new AnxietyTriggers();
        triggers.setAnxiousFear(1);
        service.recordUpdated(triggers);

        assertTrue(triggers.getClass().isAnnotationPresent(SensitiveData.class));
        assertTrue(service.exportNeeded());
    }



}
