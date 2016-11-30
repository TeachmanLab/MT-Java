package edu.virginia.psyc.mobile.persistence;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.SecureQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="DASS21AS")
@EqualsAndHashCode(callSuper = true)
@Data
public class DASS21AS extends SecureQuestionnaireData {
    private int dryness;
    private int breathing;
    private int trembling;
    private int worry;
    private int panic;
    private int heart;
    private int scared;

}

