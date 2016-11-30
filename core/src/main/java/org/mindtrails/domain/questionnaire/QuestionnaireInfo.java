package org.mindtrails.domain.questionnaire;

import lombok.Data;

/**
 * Created by dan on 10/23/15.
 */
@Data
public class QuestionnaireInfo {
    private final String name;
    private final long   size;
    private final boolean deleteable;
}
