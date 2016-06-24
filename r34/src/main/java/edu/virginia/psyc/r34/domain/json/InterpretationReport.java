package edu.virginia.psyc.r34.domain.json;

import lombok.Data;

/**
 * Created by dan on 1/25/16.
 */
@Data
public class InterpretationReport {
    private long id;
    private String participant;
    private String sessionName;
    private String scriptName;
    private String session;
    private String trial;
    private String positive;
    private String firstLetterLatency;
    private String letterLatency;
    private String letterCorrect;
    private String firstQuestionLatency;
    private String questionLatency;
    private String questionCorrect;
    private String word;
    private String paragraph;
    private String question;
    private String vividness;
}
