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
@Table(name="AUDIT")
@EqualsAndHashCode(callSuper = true)
@Data
public class AUDIT extends QuestionnaireData {

    private int drink_alc;
    private int drinks_freq;
    private int binge;
    private int cant_stop;
    private int fail;
    private int drink_morning;
    private int guilt;
    private int remembered;
    private int injured;
    private int friend;

}
