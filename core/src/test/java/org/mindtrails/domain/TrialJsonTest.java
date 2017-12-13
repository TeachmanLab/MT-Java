package org.mindtrails.domain;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindtrails.domain.piPlayer.InterpretationReport;
import org.mindtrails.domain.piPlayer.TrialJson;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/20/14
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrialJsonTest {

    // Here is a typical Json string, as provided by the PiPlayer
    public static final String JSON_STRING = "{\"log_serial\":22,\"trial_id\":24,\"name\":\"IAT\",\"responseHandle\":\"left\",\"latency\":897302,\"stimuli\":[\"Good Words\"],\"media\":[\"Happy\"],\"data\":{\"score\":0,\"block\":2,\"left1\":\"Good Words\",\"right1\":\"Bad Words\",\"conditioning\":\"Good Words/Bad Words\"}}";

    public static final String JSON_INT = "{\"log_serial\":3,\"trial_id\":\"trial_39\"," +
            "\"name\":\"posneg\"," +
            "\"responseHandle\":\"answered\"," +
            "\"latency\":3225,\"stimuli\":[\"error\"," +
            "\"yesno\",\"paragraph\",\"question\"]," +
            "\"media\":[\"X\",\"Type \\\"y\\\" for Yes, and \\\"n\\\" for No.\",\"<div><%= stimulusData.statement %><span class='incomplete'><%= trialData.positive ? stimulusData.positiveWord : stimulusData.negativeWord %></span></div>\",\"<div>Would you expect to feel uncomfortable if others look at your work?</div>\"]," +
            "\"data\":{\n" +
            "    \"positive\":false, \n" +
            "    \"correctOnLetter\":true, \n" +
            "    \"word\":\"ter[r]ible.\", \n" +
            "    \"paragraph\": \"You are out to dinner on a date. As you lo... is ter[r]ible.\", \n" +
            "    \"letter_latency\":6702, \n" +
            "    \"first_letter_latency\":6201, \n" +
            "    \"correctOnQuestion\":false, \n" +
            "    \"first_question_latency\":3498, \n" +
            "    \"question\":\"Is it fun to experience a little uncertainty on a date?\" , \n" +
            "    \"question_latency\":9985}}}";


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
        System.out.print("The trial is:" + trial.toString());
        InterpretationReport report;
        report = trial.toInterpretationReport();

        assertEquals("false", report.getPositive());
        assertEquals("true", report.getLetterCorrect());
        assertEquals("ter[r]ible.", report.getWord());
        assertEquals("You are out to dinner on a date. As you lo... is ter[r]ible.", report.getParagraph());
        assertEquals("6702", report.getLetterLatency());
        assertEquals("6201", report.getFirstLetterLatency());
        assertEquals("false", report.getQuestionCorrect());
        assertEquals("3498", report.getFirstQuestionLatency());
        assertEquals("Is it fun to experience a little uncertainty on a date?", report.getQuestion());
        assertEquals("9985", report.getQuestionLatency());

    }

}
