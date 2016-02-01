package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="FollowUp")
@EqualsAndHashCode(callSuper = true)
@Data
public class FollowUp_ChangeInTreatment extends QuestionnaireData {

    private int medication_tx;
    @Column(name="description")  // describe is a reserved word, and not a valid column name.
    private String describe;

}