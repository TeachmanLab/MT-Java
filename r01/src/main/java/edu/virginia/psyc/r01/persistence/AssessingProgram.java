package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="AssessingProgram")
@EqualsAndHashCode(callSuper = true)
@Data
public class AssessingProgram extends LinkedQuestionnaireData {

    @NotNull
    @Size(min=1)
    String worthPerWeek;
    @NotNull
    @Size(min=1)
    String compareToOthers;
}

