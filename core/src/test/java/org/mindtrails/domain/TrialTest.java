package org.mindtrails.domain;

import org.mindtrails.domain.piPlayer.Trial;
import org.mindtrails.domain.piPlayer.TrialJson;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/18/14
 * Time: 6:58 AM
 * Assure that Trials are correctly stored and retrieved from the database.
 */
public class TrialTest {

    // Here is a typical Json string, as provided by the PiPlayer
    public static final String JSON_STRING = "{\"log_serial\":22,\"trial_id\":24,\"name\":\"IAT\",\"responseHandle\":\"left\",\"latency\":897302,\"stimuli\":[\"Good Words\"],\"media\":[\"Happy\"],\"data\":{\"score\":0,\"block\":2,\"left1\":\"Good Words\",\"right1\":\"Bad Words\",\"condition\":\"Good Words/Bad Words\"}}";


    public TrialJson getTrial() {
        return(TrialJsonTest.fromJson(JSON_STRING));
    }

    @Test
    public void testCovertingTrailJson() {

        Trial trial = new Trial(getTrial());
        Assert.assertEquals(22, trial.getLog_serial());
        Assert.assertEquals(897302, trial.getLatency());
        Assert.assertEquals(1, trial.getStimuliAsList().size());
        Assert.assertEquals("Good Words", trial.getStimuliAsList().get(0));
        Assert.assertEquals(5, trial.getDataAsMap().size());
        Assert.assertEquals("0", trial.getDataAsMap().get("score"));
    }


}