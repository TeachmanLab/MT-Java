package org.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Anna on 10/23/20
 */

 // Can occur if someone tries to join the study without clicking the link in their email.
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "You can only join the Kaiser study using the link sent to your email.")
public class NoConditionSpecifiedException extends RuntimeException {}
