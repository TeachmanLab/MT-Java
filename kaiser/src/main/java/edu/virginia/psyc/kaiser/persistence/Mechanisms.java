package edu.virginia.psyc.kaiser.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="Mechanisms")
@EqualsAndHashCode(callSuper = true)
@Data
public class Mechanisms extends LinkedQuestionnaireData {

    @NotNull
    private Integer cfiViewpoints;
    @NotNull
    private Integer compActWilling;
    @NotNull
    private Integer erqPositive;
    @NotNull
    private Integer erqNegative;
    @NotNull
    private Integer iusUnexpected;
    @NotNull
    private Integer iusUncertain;
}

