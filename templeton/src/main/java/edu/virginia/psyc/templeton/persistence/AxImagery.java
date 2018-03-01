package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="AxImagery")
@EqualsAndHashCode(callSuper = true)
@Data
public class AxImagery extends LinkedQuestionnaireData {
    private String AxSituation;
    private int Vividness;
    private int TurnOut;
    private int Manageable;

}

