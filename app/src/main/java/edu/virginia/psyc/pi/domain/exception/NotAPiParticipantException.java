package edu.virginia.psyc.pi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to convert generic MindTrails Participant into a Study specific participant model.")
public class NotAPiParticipantException extends RuntimeException {

    public NotAPiParticipantException() {
        super();
    }

    public NotAPiParticipantException(Exception e) {
        super(e);
    }
}
