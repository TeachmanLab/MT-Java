package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
/**
 * Created by Diheng on 8/31/15.
 */

@Entity
@Table(name="SUDS")
@Data

public class SUDS implements QuestionnaireData{
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private ParticipantDAO participantDAO;
    private Date date;
    private String session;

    private int anxiety;



}
