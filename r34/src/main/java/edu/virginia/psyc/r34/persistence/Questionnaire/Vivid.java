package edu.virginia.psyc.r34.persistence.Questionnaire;

import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by Diheng on 8/31/15.
 */
@Entity
@Table(name="Vivid")
@EqualsAndHashCode(callSuper = true)
@Data
public class Vivid extends LinkedQuestionnaireData {
    private int vivid;
}