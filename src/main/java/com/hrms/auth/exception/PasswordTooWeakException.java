package com.hrms.auth.exception;

public class PasswordTooWeakException extends RuntimeException {
    public PasswordTooWeakException(String msg) { super(msg); }
}
