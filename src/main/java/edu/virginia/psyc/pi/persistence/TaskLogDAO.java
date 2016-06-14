package edu.virginia.psyc.pi.persistence;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dan
 * Date: 7/24/14
 * Time: 9:33 AM
 * Logs the date and time a particular task was completed by the user
 */
@Entity
@Table(name="task_log")
public class TaskLogDAO implements Comparable<TaskLogDAO>{

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private ParticipantDAO participantDAO;

    private String sessionName;
    private String taskName;
    private Date dateCompleted;

    public TaskLogDAO() {};

    public TaskLogDAO(ParticipantDAO participantDAO, String sessionName, String taskName) {
        this.participantDAO = participantDAO;
        this.sessionName = sessionName;
        this.taskName = taskName;
        this.dateCompleted = new Date();
    }

    /****************************************
     *   Generated Methods follow
     ******************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParticipantDAO getParticipantDAO() {
        return participantDAO;
    }

    public void setParticipantDAO(ParticipantDAO participantDAO) {
        this.participantDAO = participantDAO;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Override
    public int compareTo(TaskLogDAO o) {
        if(this.dateCompleted == null) return 0;
        return this.dateCompleted.compareTo(o.dateCompleted);
    }
}
