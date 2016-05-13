package edu.virginia.psyc.pi.persistence.Questionnaire;

import edu.virginia.psyc.mindtrails.domain.questionnaire.SecureQuestionnaireData;
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
public class Vivid extends SecureQuestionnaireData {
    private int vivid;
}