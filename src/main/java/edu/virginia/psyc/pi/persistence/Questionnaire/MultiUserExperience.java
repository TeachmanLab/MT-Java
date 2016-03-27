package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.pi.domain.Session;
import edu.virginia.psyc.pi.persistence.ParticipantDAO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by samportnow on 7/23/14.
 */
@Entity
@Table(name="MultiUserExperience")
@EqualsAndHashCode(callSuper = true)
@Data
public class MultiUserExperience extends QuestionnaireData {

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
    @Column(name="where_value") // 'where' is a reserved word in the database syntax and can't be used as name.
    private String where;
    private String OtherDesc;
    private String cntrl_tx;

}
