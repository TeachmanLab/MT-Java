package org.mindtrails.domain.tracking;

import org.mindtrails.domain.BaseStudy;
import org.mindtrails.domain.DoNotDelete;
import org.mindtrails.domain.Exportable;
import org.mindtrails.domain.Study;
import lombok.Data;

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
public class TaskLog implements Comparable<TaskLog> {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity=BaseStudy.class)
    private Study study;

    private String sessionName;
    private String taskName;
    private Date dateCompleted;

    public TaskLog() {};

    public TaskLog(Study study) {
        this.study = study;
        this.sessionName = study.getCurrentSession().getName();
        this.taskName = study.getCurrentSession().getCurrentTask().getName();
        this.dateCompleted = new Date();
    }

    @Override
    public int compareTo(TaskLog o) {
        if(this.dateCompleted == null) return 0;
        return this.dateCompleted.compareTo(o.dateCompleted);
    }

}
