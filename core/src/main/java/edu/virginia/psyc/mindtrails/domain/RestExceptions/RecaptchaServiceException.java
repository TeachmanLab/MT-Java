package edu.virginia.psyc.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to validate Re-Captcha - service may be down.")
public class RecaptchaServiceException extends RuntimeException {

    public RecaptchaServiceException(String message, Exception e) {
        super(message, e);
    }

}
