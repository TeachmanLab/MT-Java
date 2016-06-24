package org.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN, reason = "This data must remain on the server, and cannot be removed.")
public class NotDeleteableException extends RuntimeException {}
