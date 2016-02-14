package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
/**
 * Created by Diheng on 8/31/15.
 */

@Entity
@Table(name="SUDS")
@EqualsAndHashCode(callSuper = true)
@Data
public class SUDS extends QuestionnaireData {

    private int anxiety;

}
