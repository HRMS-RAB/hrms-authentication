package com.hrms.auth.service;

/**
 * Contract for all password-reset operations.
 */
public interface PasswordResetService {

    /** Send (or log) a reset token for the given e-mail. */
    void forgot(String workEmail);

    /** Validate the token and overwrite the password. */
    void confirm(String token, String newPwd);

    /** Authenticated change using the old password as proof. */
    void change(String username, String currentPwd, String newPwd);
}
