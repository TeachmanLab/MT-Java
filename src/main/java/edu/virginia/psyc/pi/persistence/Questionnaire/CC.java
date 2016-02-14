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
@Table(name="CC")
@EqualsAndHashCode(callSuper = true)
@Data
public class CC extends QuestionnaireData {

    private int related;
    private int compare;

}