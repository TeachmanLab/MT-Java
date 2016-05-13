package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="PilotUserExperience")
@EqualsAndHashCode(callSuper = true)
@Data
public class PilotUserExperience extends SecureQuestionnaireData {

    private int ease;
    private int interest;
    @Column(name="like_value") // 'like' is a reserved word in the database syntax and can't be used as name.
    private int like;
    private int look;
    private int privacy;
    private int understand;
    private int trust;
    private int practice;
    private int treat_anx;
    private int recommend;
    private int tiring_training;
    private int tiring_measures;
    private int continue_program;

}
