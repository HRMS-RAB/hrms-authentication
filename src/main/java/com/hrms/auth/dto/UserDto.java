package com.hrms.auth.dto;

import java.util.Set;

public class UserDto {

    private Long userId;
    private Long employeeId;
    private String workEmail;
    private String fullName;
    private Boolean isAdmin;
    private Set<String> roles;   // role names only

    /* Getters & setters */
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean admin) { isAdmin = admin; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}





/*package com.hrms.auth.dto;

import java.util.Set;

public class UserDto {
    private Long userId;
    private String workEmail;
    private String fullName;
    private Set<String> roles;

    // getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}*/
