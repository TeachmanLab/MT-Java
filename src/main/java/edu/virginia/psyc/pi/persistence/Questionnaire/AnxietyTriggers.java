package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="AnxietyTriggers")
@Data
public class AnxietyTriggers implements QuestionnaireData {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private String howLong;
    private int social;
    private int sensations;
    private int worry;
    private int anxiousFear;
    private int particularObject;
    private int thoughts;
    private int priorTrauma;

}
