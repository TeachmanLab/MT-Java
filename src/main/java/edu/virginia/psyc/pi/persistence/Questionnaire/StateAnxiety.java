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
@Table(name="SA")
@Data
public class StateAnxiety extends QuestionnaireData {

    private String situation_desc;
    private int think_feel;
    private int anxious;
    private int badly;
    private int terrible;
    private int vivid;

    private String situation_desc_2;
    private int think_feel_2;
    private int anxious_2;
    private int badly_2;
    private int terrible_2;
    private int vivid2;

}

