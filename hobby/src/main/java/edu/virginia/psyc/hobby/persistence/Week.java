package edu.virginia.psyc.hobby.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by saragomezt on 7/11/16.
 */

@Entity
@Table(name="Week")
@EqualsAndHashCode(callSuper = true)
@Data
public class Week extends SecureQuestionnaireData {
    private int Sandwich;
    private int Drinks;
    private int Chips;
    private int Fruit;

}