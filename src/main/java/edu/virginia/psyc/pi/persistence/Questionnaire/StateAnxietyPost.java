package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="StateAnxietyPost")
@Data
public class StateAnxietyPost extends QuestionnaireData{

    private String situation_desc;
    private int think_feel;
    private int anxious;
    private int avoid;
    private int badly;
    private int terrible;

}

