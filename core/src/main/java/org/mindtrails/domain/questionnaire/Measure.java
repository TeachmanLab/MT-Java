package org.mindtrails.domain.questionnaire;

import lombok.Data;

@Data
public class Measure {
    protected final String name;
    protected final String desc;
    private boolean error = false;
    private String errorMessage;

}