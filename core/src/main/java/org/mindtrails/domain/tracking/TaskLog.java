package org.mindtrails.domain.tracking;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.mindtrails.domain.*;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs the date and time a particular task was completed by the user, bastically
 * taking a snapshot of the users progress through a task.
 */
@Entity
@Table(name="task_log")
@Exportable
@DoNotDelete
@Data
public class TaskLog implements Comparable<TaskLog>, hasStudy {

    private static String SESSION_COMPLETE = "SESSION_COMPLETE";

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity=BaseStudy.class)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
            property="id")
    @JsonIdentityReference(alwaysAsId=true) // otherwise first ref as POJO, others as id
    @JsonProperty(value = "study")
    private Study study;
    private String sessionName;
    private String taskName;
    private Date dateCompleted;
    private String tag;
    private Double timeOnTask;

    public TaskLog() {};
    public TaskLog(Study study, double timeOnTask) {
        this.study = study;
        this.sessionName = study.getCurrentSession().getName();
        this.taskName = study.getCurrentSession().getCurrentTask().getName();
        this.dateCompleted = new Date();
        this.tag = study.getCurrentSession().getCurrentTask().getTag();
        this.timeOnTask = timeOnTask;
    }

    public static TaskLog completedSession(Study study) {
        TaskLog log = new TaskLog();
        log.study = study;
        log.sessionName = study.getCurrentSession().getName();
        log.taskName = SESSION_COMPLETE;
        log.dateCompleted = new Date();
        log.tag = null;
        log.timeOnTask = null;
        return(log);
    }


    @Override
    public int compareTo(TaskLog o) {
        if(this.dateCompleted == null) return 0;
        return this.dateCompleted.compareTo(o.dateCompleted);
    }


}
