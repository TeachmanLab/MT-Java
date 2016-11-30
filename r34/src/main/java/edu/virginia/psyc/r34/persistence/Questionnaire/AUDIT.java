package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="AUDIT")
@EqualsAndHashCode(callSuper = true)
@Data
public class AUDIT extends SecureQuestionnaireData {

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
