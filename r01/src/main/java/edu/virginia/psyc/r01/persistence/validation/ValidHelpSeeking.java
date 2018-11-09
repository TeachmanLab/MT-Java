package edu.virginia.psyc.r01.persistence.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Some documentation on validation is available here:
 * https://www.logicbig.com/tutorials/java-ee-tutorial/bean-validation/class-level-constraints.html
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HelpSeekingValidator.class)
@Documented
public @interface ValidHelpSeeking {
    String message () default "At least one selection must be made." +
            "Found: ${validatedValue.totalPrice}";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}