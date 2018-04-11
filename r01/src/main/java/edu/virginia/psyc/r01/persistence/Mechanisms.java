package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Mechanisms")
@EqualsAndHashCode(callSuper = true)
@Data
public class Mechanisms extends LinkedQuestionnaireData {
    private int cfiViewpoints;
    private int compActWilling;
    private int erqPositive;
    private int erqNegative;
    private int iusUnexpected;
    private int iusUncertain;
}

