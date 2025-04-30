package com.hrms.auth.listener;

public class EmployeeEvent {

    private Long   employeeId;
    private Long   departmentId;
    private String departmentName;
    private String fullName;
    private String workEmail;
    private String gradeLevel;
    private String employeeType;
    private String eventType;

    /* ───── getters / setters ───── */

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
