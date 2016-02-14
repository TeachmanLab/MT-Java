package edu.virginia.psyc.pi.persistence.Questionnaire;

import javax.persistence.Entity;
import javax.persistence.Table;


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
@Table(name="ImpactAnxiousImagery")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImpactAnxiousImagery extends QuestionnaireData{

    private int anxiety;
    private int vivid;
    private int badly;
    private int manageable;

}
