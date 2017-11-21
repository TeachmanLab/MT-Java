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

@Entity
@Table(name="PBS")
@EqualsAndHashCode(callSuper = true)
@Data



public class PBS extends SecureQuestionnaireData {
    private int think_really;
    private int person_basic;
    private int think_quite;
    private int person_important;
    private int person_substantially;
    private int person_certain;
    private int think_substantially;
    private int think_basic;
}
