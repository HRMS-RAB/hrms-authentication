package com.hrms.auth.listener;

/**
 * Immutable POJO that mirrors the EmployeeEvent published by hrms-backend.
 * Keep the field names identical so Jackson can deserialise automatically.
 */
public class EmployeeEvent {

    private Long   employeeId;
    private Long   departmentId;
    private String departmentName;
    private String fullName;
    private String workEmail;
    private String gradeLevel;
    private String employeeType;
    private String eventType;

    /* ─── getters / setters ─── */

    public Long   getEmployeeId()             { return employeeId; }
    public void   setEmployeeId(Long id)      { this.employeeId = id; }

    public Long   getDepartmentId()           { return departmentId; }
    public void   setDepartmentId(Long id)    { this.departmentId = id; }

    public String getDepartmentName()         { return departmentName; }
    public void   setDepartmentName(String n) { this.departmentName = n; }

    public String getFullName()               { return fullName; }
    public void   setFullName(String n)       { this.fullName = n; }

    public String getWorkEmail()              { return workEmail; }
    public void   setWorkEmail(String e)      { this.workEmail = e; }

    public String getGradeLevel()             { return gradeLevel; }
    public void   setGradeLevel(String g)     { this.gradeLevel = g; }

    public String getEmployeeType()           { return employeeType; }
    public void   setEmployeeType(String t)   { this.employeeType = t; }

    public String getEventType()              { return eventType; }
    public void   setEventType(String t)      { this.eventType = t; }
}


/*package com.hrms.auth.listener;

public class EmployeeEvent {

    private Long   employeeId;
    private Long   departmentId;
    private String departmentName;
    private String fullName;
    private String workEmail;
    private String gradeLevel;
    private String employeeType;
    private String eventType;

    /* ───── getters / setters ───── 

    public Long   getEmployeeId()      { return employeeId; }
    public void   setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public Long   getDepartmentId()    { return departmentId; }
    public void   setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName()  { return departmentName; }
    public void   setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getFullName()        { return fullName; }
    public void   setFullName(String fullName) { this.fullName = fullName; }

    public String getWorkEmail()       { return workEmail; }
    public void   setWorkEmail(String workEmail) { this.workEmail = workEmail; }

    public String getGradeLevel()      { return gradeLevel; }
    public void   setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getEmployeeType()    { return employeeType; }        // correct camelCase
    public void   setEmployeeType(String employeeType) { this.employeeType = employeeType; }

    public String getEventType()       { return eventType; }
    public void   setEventType(String eventType) { this.eventType = eventType; }
}
*/