package edu.virginia.psyc.pi.domain;

import lombok.Data;

import java.util.Date;

/**
 * For recording the completion of tasks, mostly useful for display back to the user.
 */
@Data
public class TaskLog {
    private final String sessionName;
    private final String taskName;
    private final Date dateCompleted;
}

