package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="CC")
@Data
public class CC implements QuestionnaireData {


    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;
    private int related;
    private int compare;

}