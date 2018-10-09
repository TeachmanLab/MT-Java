package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dan on 8/27/15.
 */
@Entity
@Table(name="Demographics")
@EqualsAndHashCode(callSuper = true)
@Data
public class Demographics extends LinkedQuestionnaireData {
    @NotNull
    private Integer BirthYear;
    @NotNull
    private String Ethnicity;
    @NotNull
    private String Country;
    @NotNull
    private String Education;
    @NotNull
    private String MaritalStat;
    @NotNull
    private String EmploymentStat;
    @NotNull
    private String Income;
    @NotNull
    private String PtpReason;
    @NotNull
    private String PtpReasonOther;
    @NotNull
    private String Gender;

    @ElementCollection
    @CollectionTable(name = "demographics_race", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "race")
    @NotNull
    private List<String> Race;
}