package edu.virginia.psyc.r01.persistence.validation;

import edu.virginia.psyc.r01.persistence.HelpSeeking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Assures that at least one value is selected.
 */
public class HelpSeekingValidator implements ConstraintValidator<ValidHelpSeeking, HelpSeeking> {

    @Override
    public void initialize (ValidHelpSeeking constraintAnnotation) {
    }

    @Override
    public boolean isValid (HelpSeeking hs,
                            ConstraintValidatorContext context) {

        return hs.isMorePerson() || hs.isLessPerson() ||
                hs.isMoreMeds() || hs.isLessMeds() ||
                hs.isMoreApps() || hs.isLessApps() ||
                hs.isNoChange() || hs.isOtherChange() ||
                hs.isNoAnswer();
    }
}