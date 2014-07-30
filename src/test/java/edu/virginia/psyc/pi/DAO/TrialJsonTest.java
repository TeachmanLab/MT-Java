package edu.virginia.psyc.pi.DAO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.virginia.psyc.pi.domain.json.TrialJson;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

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

    public static final String JSON_INT = "{\"log_serial\":3,\"trial_id\":\"trial_39\"," +
            "\"name\":\"posneg\"," +
            "\"responseHandle\":\"answered\"," +
            "\"latency\":3225,\"stimuli\":[\"error\"," +
            "\"yesno\",\"paragraph\",\"question\"]," +
            "\"media\":[\"X\",\"Type \\\"y\\\" for Yes, and \\\"n\\\" for No.\",\"<div><%= stimulusData.statement %><span class='incomplete'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>\",\"<div>Would you expect to feel uncomfortable if others look at your work?</div>\"]," +
            "\"data\":{\"positive\":true," +
            "\"paragraph\":\"A friend suggests that you join an evening class on creative writing. The thought of other people looking at your writing makes you feel enthu[s]iastic\"," +
            "\"questionResponse\":\"yes\"," +
            "\"question\":\"Would you expect to feel uncomfortable if others look at your work?\"}}";


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

    @Test
    public void testInterpretationReport() {
        TrialJson trial = fromJson(JSON_INT);

        Map<String,String> report;

        report = trial.toInterpretationReport();

        Assert.assertTrue(report.keySet().contains("session"));
        Assert.assertTrue(report.keySet().contains("trial"));
        Assert.assertTrue(report.keySet().contains("positive"));
        Assert.assertTrue(report.keySet().contains("correct"));
        Assert.assertTrue(report.keySet().contains("paragraph"));
        Assert.assertTrue(report.keySet().contains("question"));


    }

}
