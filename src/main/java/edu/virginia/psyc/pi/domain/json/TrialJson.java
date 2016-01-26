package edu.virginia.psyc.pi.domain.json;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/10/14
 * Time: 10:28 PM
 * An Java Object representation of data returned from PIPlayer.  This representation
 * is designed for storing in a relational database.
 */
@Data
public class TrialJson {

    private long id;
    private int log_serial;
    private String trial_id;
    private String name;
    private String responseHandle;
    private int latency;
    private List<String> stimuli;
    private List<String> media;
    private Map<String,String> data;
    private String script;
    private String session;
    private int participant;

    @Override
    public String toString() {
        return "TrialJson{" +
                "id=" + id +
                ", log_serial=" + log_serial +
                ", trial_id='" + trial_id + '\'' +
                ", name='" + name + '\'' +
                ", responseHandle='" + responseHandle + '\'' +
                ", latency=" + latency +
                ", stimuli=" + stimuli +
                ", media=" + media +
                ", data=" + data +
                ", script='" + script + '\'' +
                ", session='" + session + '\'' +
                ", participant=" + participant +
                '}';
    }


    public InterpretationReport toInterpretationReport() {
        InterpretationReport r = new InterpretationReport();
        r.setId(id);
        r.setParticipant(participant + "");
        r.setSessionName(session);
        r.setScriptName(script);
        r.setSession(log_serial + "");
        r.setTrial(trial_id);
        r.setPositive(data.get("positive"));
        r.setFirstLetterLatency(data.get("first_letter_latency"));
        r.setLetterLatency(data.get("letter_latency"));
        r.setLetterCorrect(data.get("correctOnLetter"));
        r.setFirstQuestionLatency(data.get("first_question_latency"));
        r.setQuestionLatency(data.get("question_latency"));
        r.setQuestionCorrect(data.get("correctOnQuestion"));
        r.setWord(data.get("word"));
        r.setParagraph(data.get("paragraph"));
        r.setQuestion(data.get("question"));
        return r;
    }

}