package com.hrms.auth.exception;

/** Thrown when a user supplies an incorrect current password. */
public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
