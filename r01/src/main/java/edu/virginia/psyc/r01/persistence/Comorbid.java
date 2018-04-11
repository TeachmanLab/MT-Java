package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="comorbid")
@EqualsAndHashCode(callSuper = true)
@Data
public class Comorbid extends LinkedQuestionnaireData {
    private int pleasure;
    private int depressed;
    private int howOften;
    private int numberOfDrinks;
    private int sixOrMore;
}

