package edu.virginia.psyc.pi.persistence.Questionnaire;

import lombok.Data;
import javax.persistence.*;

/**
 * Created by Diheng on 8/31/15.
 */
@Entity
@Table(name="Vivid")
@Data
public class Vivid extends QuestionnaireData {
    private int vivid;
}