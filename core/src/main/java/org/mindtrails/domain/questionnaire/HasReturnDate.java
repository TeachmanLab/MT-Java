package org.mindtrails.domain.questionnaire;


import java.util.Date;

/**
 * Questionnaires can implement this interface to cause a calendar invite to be fired
 * in an email upon submission.
 */
public interface HasReturnDate {

    Date getReturnDate();

}
