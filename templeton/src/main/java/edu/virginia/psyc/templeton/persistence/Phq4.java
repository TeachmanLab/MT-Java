package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Phq4")
@EqualsAndHashCode(callSuper = true)
@Data
public class Phq4 extends LinkedQuestionnaireData {
    private int nervous;
    private int worry;
    private int pleasure;
    private int depressed;
}

