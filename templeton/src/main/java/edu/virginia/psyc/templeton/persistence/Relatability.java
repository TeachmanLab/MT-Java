package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Relatability")
@EqualsAndHashCode(callSuper = true)
@Data
public class Relatability extends LinkedQuestionnaireData {
    private int Relate;
    private int Behaving;
}

