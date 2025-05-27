package com.hrms.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a reset token is missing, expired, or the current password
 * check fails.  Spring turns it into HTTP 403.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) { super(message); }
}
