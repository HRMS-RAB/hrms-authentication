package com.hrms.auth.dto;

import java.util.Set;

/** DTO version of User (no Lombok). */
public class UserDto {

    private Long        userId;
    private Long        employeeId;
    private Long        deptId;
    private String      deptName;
    private String      fullName;
    private String      workEmail;
    private String      gradeLevel;   // ← NEW
    private Set<String> roles;
    private Boolean     isAdmin;
    private Boolean     active;

    /* getters / setters */
    public Long getUserId()               { return userId; }
    public void setUserId(Long v)         { this.userId = v; }

    public Long getEmployeeId()           { return employeeId; }
    public void setEmployeeId(Long v)     { this.employeeId = v; }

    public Long getDeptId()               { return deptId; }
    public void setDeptId(Long v)         { this.deptId = v; }

    public String getDeptName()           { return deptName; }
    public void setDeptName(String v)     { this.deptName = v; }

    public String getFullName()           { return fullName; }
    public void setFullName(String v)     { this.fullName = v; }

    public String getWorkEmail()          { return workEmail; }
    public void setWorkEmail(String v)    { this.workEmail = v; }

    public String getGradeLevel()         { return gradeLevel; }
    public void setGradeLevel(String v)   { this.gradeLevel = v; }

    public Set<String> getRoles()         { return roles; }
    public void setRoles(Set<String> v)   { this.roles = v; }

    public Boolean getIsAdmin()           { return isAdmin; }
    public void setIsAdmin(Boolean v)     { this.isAdmin = v; }

    public Boolean getActive()            { return active; }
    public void setActive(Boolean v)      { this.active = v; }
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
