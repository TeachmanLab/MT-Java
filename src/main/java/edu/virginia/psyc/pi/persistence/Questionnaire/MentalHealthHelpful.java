package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MentalHealthHelps")
@Data
public class MentalHealthHelpful {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private MentalHealthHxTx mentalHealthHxTx;
    private String type;
    private int amount;
}

