package edu.virginia.psyc.pi.DAO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.rest.SequenceJson;
import edu.virginia.psyc.pi.rest.TrialJson;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrialJsonTest {

    // Here is a typical Json string, as provided by the PiPlayer
    public static final String JSON_STRING = "{\"log_serial\":22,\"trial_id\":24,\"name\":\"IAT\",\"responseHandle\":\"left\",\"latency\":897302,\"stimuli\":[\"Good Words\"],\"media\":[\"Happy\"],\"data\":{\"score\":0,\"block\":2,\"left1\":\"Good Words\",\"right1\":\"Bad Words\",\"condition\":\"Good Words/Bad Words\"}}";


    public static TrialJson fromJson(String text) throws IllegalArgumentException {
        ObjectMapper mapper = new ObjectMapper();
        TrialJson    trialJson   = new TrialJson();
        try {
            trialJson = mapper.readValue(text, TrialJson.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JsonParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return trialJson;
    }

    @Test
    public void TrialFromJsonContainsCorrectData() {

        TrialJson trial = fromJson(JSON_STRING);

        Assert.assertEquals(22, trial.getLog_serial());
        Assert.assertEquals(897302, trial.getLatency());
        Assert.assertEquals("Good Words", trial.getStimuli().get(0));
        Assert.assertEquals("0", trial.getData().get("score"));
    }

}
