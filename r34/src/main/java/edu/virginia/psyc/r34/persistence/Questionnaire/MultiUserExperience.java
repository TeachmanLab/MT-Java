package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MultiUserExperience")
@EqualsAndHashCode(callSuper = true)
@Data
public class MultiUserExperience extends SecureQuestionnaireData {

    private int helpful;
    private int qol;
    private int mood;
    private int recommend;
    private int ease;
    private int interest;
    @Column(name="like_value") // 'like' is a reserved word in the database syntax and can't be used as name.
    private int like;
    private int look;
    private int privacy;
    private int understand;
    private int training_ease;
    private int trust;
    private int internet;
    private int ideal;
    @Column(name="`describe`")
    private String describe;
    private int tiring_training;
    private int distracted;
    private int similar_program;
    private int other_therapy;
    private boolean where_no_anser;
    private boolean where_home;
    private boolean where_work;
    private boolean where_public;
    private boolean where_commuting;
    private boolean where_vacation;
    private boolean where_other_selected;
    private String OtherDesc;
    private String cntrl_tx;

}
