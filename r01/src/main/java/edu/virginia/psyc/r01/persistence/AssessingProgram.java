package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="AssessingProgram")
@EqualsAndHashCode(callSuper = true)
@Data
public class AssessingProgram extends LinkedQuestionnaireData {

    String worthPerWeek;
    String compareToOthers;
}

