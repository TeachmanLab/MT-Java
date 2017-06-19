package org.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "You must complete an eligibility form to continue.")
public class MissingEligibilityException extends RuntimeException {

    public MissingEligibilityException() {}

    public MissingEligibilityException(String message) {
        super(message);
    }

}
