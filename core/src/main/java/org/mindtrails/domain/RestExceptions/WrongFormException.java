package org.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "You did not submit the correct form.")
public class WrongFormException extends RuntimeException {

    public WrongFormException() {}

    public WrongFormException(String message) {
        super(message);
    }

}
