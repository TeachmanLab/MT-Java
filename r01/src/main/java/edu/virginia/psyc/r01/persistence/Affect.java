package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="Affect")
@EqualsAndHashCode(callSuper = true)
@Data
public class Affect extends LinkedQuestionnaireData {
    private int PosFeelings;
    private int NegFeelings;
}

