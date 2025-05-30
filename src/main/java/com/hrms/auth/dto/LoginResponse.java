package com.hrms.auth.dto;

/**
 * Simple POJO returned to the frontend after /auth/login.
 * Immutable + builder-style constructor to avoid setters.
 */
public class LoginResponse {

    private final String token;
    private final String username;
    private final String fullName;

    public LoginResponse(String token, String username, String fullName) {
        this.token    = token;
        this.username = username;
        this.fullName = fullName;
    }

    public String getToken()    { return token; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
}





/*package com.hrms.auth.dto;

public class LoginResponse {
    private String token;
    public LoginResponse(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
*/