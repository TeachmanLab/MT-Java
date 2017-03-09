package edu.virginia.psyc.templeton.persistence;

import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="Demographics")
@EqualsAndHashCode(callSuper = true)
@Data
public class Demographics extends SecureQuestionnaireData {
    private String GenderId;
    private int BirthYear;
    private String Race;
    private String Ethnicity;
    private String Country;
    private String Education;
    private String MaritalStat;
    private String EmploymentStat;
    private String Income;
    private String PtpReason;
    private String PtpReasonOther;

}

