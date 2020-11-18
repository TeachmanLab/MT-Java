package edu.virginia.psyc.r01.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import org.mindtrails.domain.questionnaire.MeasureField;

import javax.persistence.*;


@Entity
@Table(name="Acculturation")
@EqualsAndHashCode(callSuper = true)
@Data
public class Acculturation extends LinkedQuestionnaireData {

}



