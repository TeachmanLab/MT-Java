package org.mindtrails.domain.Conditions;

import lombok.Data;

@Data

/**
 * Simple data structure for updating the assignment of a participant based on their
 * current status.
 */
public class ConditionAssignment {

    private long participantId;
    private String condition;

    public ConditionAssignment() {}

    public ConditionAssignment(long id, String condition) {
        this.participantId = id;
        this.condition = condition;
    }

}
