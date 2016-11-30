package edu.virginia.psyc.templeton.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="DASS21DS")
@EqualsAndHashCode(callSuper = true)
@Data
public class DASS21DS extends SecureQuestionnaireData {
    private int NoPositiveFeeling;
    private int DifficultInitiative;
    private int NervousEnergy;
    private int Blue;
    private int Enthusiastic;
    private int Worth;
    private int Meaningless;

}

