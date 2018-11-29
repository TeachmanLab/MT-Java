package org.mindtrails.domain.Conditions;

/**
 * Thrown when no new assignable condition is available for a participant because we don't have adequate information
 * to make the assignment, or the participant already has a condition applied.
 */
public class NoNewConditionException extends Exception {}
