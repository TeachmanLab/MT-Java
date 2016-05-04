package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.persistence.TrialDAO;
import edu.virginia.psyc.pi.domain.json.TrialJson;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Assure that Trials are correctly stored and retrieved from the database.
 */
public class TrialDAOTest {

    // Here is a typical Json string, as provided by the PiPlayer
    public static final String JSON_STRING = "{\"log_serial\":22,\"trial_id\":24,\"name\":\"IAT\",\"responseHandle\":\"left\",\"latency\":897302,\"stimuli\":[\"Good Words\"],\"media\":[\"Happy\"],\"data\":{\"score\":0,\"block\":2,\"left1\":\"Good Words\",\"right1\":\"Bad Words\",\"condition\":\"Good Words/Bad Words\"}}";


    public TrialJson getTrial() {
        return(TrialJsonTest.fromJson(JSON_STRING));
    }

    @Test
    public void testCovertingTrailJson() {

        TrialDAO trialDao = new TrialDAO(getTrial());
        Assert.assertEquals(22, trialDao.getLog_serial());
        Assert.assertEquals(897302, trialDao.getLatency());
        Assert.assertEquals(1, trialDao.getStimuliAsList().size());
        Assert.assertEquals("Good Words", trialDao.getStimuliAsList().get(0));
        Assert.assertEquals(5, trialDao.getDataAsMap().size());
        Assert.assertEquals("0", trialDao.getDataAsMap().get("score"));
    }


}