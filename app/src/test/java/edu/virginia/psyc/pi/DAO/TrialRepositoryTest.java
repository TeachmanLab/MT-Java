package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.Application;
import edu.virginia.psyc.pi.persistence.TrialDAO;
import edu.virginia.psyc.pi.persistence.TrialRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class TrialRepositoryTest {

    @Autowired
    protected TrialRepository trialRepository;

    @Test
    @Transactional
    public void findTrials() {
        List<TrialDAO> trialDAOs;

        Assert.assertTrue(true);
        trialDAOs = trialRepository.findAll();
        Assert.assertNotNull(trialDAOs);
    }

    @Test
    @Transactional
    public void saveAndRetrieveTrial() {

        TrialDAO t, tout;

        t = new TrialDAO();
        Map data = new HashMap<String,String>();
        data.put("myKey", "myVal");

        t.setLog_serial(1234124124);
        t.setData(data);

        trialRepository.save(t);

        tout = trialRepository.findOne(t.getId());

        Assert.assertEquals(tout.getLog_serial(), t.getLog_serial());
        Assert.assertEquals(tout.getDataAsMap().get("myKey"), "myVal");

    }


}
