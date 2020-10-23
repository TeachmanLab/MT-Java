package org.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Created by Anna on 10/23/20
 */

 // Can occur if someone tries to manipulate the query params in the join study URL.
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "You can only join the Kaiser study using the link sent to your email.")
public class NoSuchConditionException extends RuntimeException {}
