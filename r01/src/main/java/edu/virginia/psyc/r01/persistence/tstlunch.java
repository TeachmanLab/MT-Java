package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by saragomezt on 7/11/16.
 */

@Entity
@Table(name="tstlunch")
@EqualsAndHashCode(callSuper = true)
@Data
public class tstlunch extends SecureQuestionnaireData {
    private int a1;
    private int a2;
    private int a3;
    private int newone;

}