package com.hrms.auth.util;

import java.util.regex.Pattern;

/**
 * Very simple strength rule:
 *  - ≥ 8 characters
 *  - at least 1 upper-case, 1 lower-case, 1 digit, 1 special.
 */
public final class PasswordValidator {

    private static final Pattern STRONG =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");

    private PasswordValidator() { /* util */ }

    public static boolean isStrong(String pwd) {
        return STRONG.matcher(pwd).matches();
    }
}
