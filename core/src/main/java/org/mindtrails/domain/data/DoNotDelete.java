package org.mindtrails.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Questionnaire DAO's that are labeled with this class should not be removed
 * after export.  This information will be communicated to clients, so they
 * know they should follow up with delete commands after they have successfully
 * backed up the data.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in method only.
public @interface DoNotDelete {}
