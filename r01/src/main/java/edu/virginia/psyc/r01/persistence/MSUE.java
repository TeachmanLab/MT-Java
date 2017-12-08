package edu.virginia.psyc.r01.persistence;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

/**
 * Created by any on 7/29/17.
 */


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="MSUE")
@EqualsAndHashCode(callSuper = true)
@Data



public class MSUE extends SecureQuestionnaireData {
    private int anxiety;
    private int life;
    private int mood;
    private int recommend;
    private int easy_use;
    private int interest;
    private int general;
    private int look;
    private int privacy;
    private int assessments;
    private int training;
    private int trust;
    private int problems;
    private String problem_desc;
    private int training_num;
    private int tiring_training;
    private int tiring_assessment;
    private int focused;
    private int similar_program;
    private int other_therapy;
    @ElementCollection
    @CollectionTable(name = "MSUE_Location", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "location")
    List<String> location;
    String location_Desc;
    boolean thoughtInControl;
    String pointInControl;
    @ElementCollection
    @CollectionTable(name = "MSUE_whyInControl", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "whyInControl")
    List<String> whyInControl;
    //String otherWhyInControl;
    String control_Desc;
    @ElementCollection
    @CollectionTable(name = "MSUE_learn", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "learn")
    List<String>learn;
    String link_Desc;
    String Other_learn_Desc;


}
