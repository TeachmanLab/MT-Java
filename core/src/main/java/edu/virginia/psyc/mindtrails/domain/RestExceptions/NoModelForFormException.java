package edu.virginia.psyc.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason = "There was a problem saving the form.  Please check the logs.")
public class NoModelForFormException extends RuntimeException {

    public NoModelForFormException() {
        super();
    }

    public NoModelForFormException(Exception e) {
        super(e);
    }
}
