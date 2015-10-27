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
public class SUDS extends QuestionnaireData {

    private int anxiety;

}
