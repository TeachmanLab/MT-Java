package org.mindtrails.domain.questionnaire;

import lombok.Data;

import java.util.Map;

@Data
public class Measure {
    protected final String name;
    protected final String desc;
    protected final Map<Integer, String> scale;  // list of potential values.
    private boolean error = false;
    private String errorMessage;
}