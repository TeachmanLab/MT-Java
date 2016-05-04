package org.virginia.psyc.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason = "There is no repository defined in the MindTrails code to store this form, please create one.")
public class NoRepositoryForFormException extends RuntimeException {}
