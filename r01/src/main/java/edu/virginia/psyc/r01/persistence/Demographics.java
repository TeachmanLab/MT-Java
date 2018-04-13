package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="Demographics")
@EqualsAndHashCode(callSuper = true)
@Data
public class Demographics extends LinkedQuestionnaireData {
    private String GenderId;
    private int BirthYear;
    private String Ethnicity;
    private String Country;
    private String Education;
    private String MaritalStat;
    private String EmploymentStat;
    private String Income;
    //private String Devices;
    private String PtpReason;
    private String PtpReasonOther;

    @ElementCollection
    @CollectionTable(name = "demographics_race", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "race")
    private List<String> Race;
}

