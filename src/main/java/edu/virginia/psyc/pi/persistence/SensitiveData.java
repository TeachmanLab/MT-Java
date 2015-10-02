package edu.virginia.psyc.pi.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Entities should apply this annotation if they contain sensitive data that
 * should be removed from the database on a regular schedule.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {}
