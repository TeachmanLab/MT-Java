package edu.virginia.psyc.r01.persistence;

import edu.virginia.psyc.r01.persistence.validation.ValidHelpSeeking;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mindtrails.domain.questionnaire.LinkedQuestionnaireData;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;

@Entity
@Table(name="HelpSeeking")
@EqualsAndHashCode(callSuper = true)
@Data
@ValidHelpSeeking
public class HelpSeeking extends LinkedQuestionnaireData {
    private boolean morePerson;
    private boolean lessPerson;
    private boolean moreMeds;
    private boolean lessMeds;
    private boolean moreApps;
    private boolean lessApps;
    private boolean noChange;
    private boolean otherChange;
    private boolean noAnswer;
    private String other;
}