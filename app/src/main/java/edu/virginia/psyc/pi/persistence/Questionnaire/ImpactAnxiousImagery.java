package edu.virginia.psyc.pi.persistence.Questionnaire;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by samportnow on 7/21/14.
 */
@Entity
@Table(name="ImpactAnxiousImagery")
@EqualsAndHashCode(callSuper = true)
@Data
public class ImpactAnxiousImagery extends SecureQuestionnaireData {

    private int anxiety;
    private int vivid;
    private int badly;
    private int manageable;

}
