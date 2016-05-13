package edu.virginia.psyc.mindtrails.domain;

import java.lang.annotation.*;

/**
 * This Annotation should be applied to any and all DAO objects that should be
 * available for export from the system.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Exportable {}
