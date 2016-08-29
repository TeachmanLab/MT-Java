package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by saragomezt on 7/11/16.
 */

@Entity
@Table(name="LunchOption")
@EqualsAndHashCode(callSuper = true)
@Data
public class LunchOption extends SecureQuestionnaireData {
    private String Lunch;

}