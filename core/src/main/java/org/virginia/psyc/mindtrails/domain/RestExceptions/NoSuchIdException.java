package org.virginia.psyc.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Questionnaire with that ID does not exist.")
public class NoSuchIdException extends RuntimeException {}
