package edu.virginia.psyc.pi.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Questionnaire DAO's that are labled with this class should be removed
 * after export.  This information will be communicated to clients, so they
 * know they should follow up with delete commands after they have successfully
 * backed up the data.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //can use in method only.
public @interface DoNotDelete {}
