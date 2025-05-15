package com.hrms.auth.entity;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

/** User entity — NO Lombok.  Keeps all original columns and adds gradeLevel. */
@Audited
@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_users_work_email",
        columnNames = "work_email"
))
public class User {

    /* ── Primary key ── */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /* ── Business keys & profile data ── */
    @Column(name = "employee_id")
    private Long employeeId;                 // nullable for admins

    private Long deptId;                     // keep your original department info
    private String deptName;
    private String fullName;

    @Column(name = "work_email", nullable = false, length = 100)
    private String workEmail;

    @Column(nullable = false)
    private String password;

    /** NEW column, used by role-assignment logic */
    @Column(name = "grade_level", length = 50)
    private String gradeLevel;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = Boolean.FALSE;

    private Boolean active = Boolean.TRUE;

    /* ── Roles ── */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns        = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /* ────────── getters / setters ────────── */
    public Long getUserId()               { return userId; }
    public void setUserId(Long id)        { this.userId = id; }

    public Long getEmployeeId()           { return employeeId; }
    public void setEmployeeId(Long id)    { this.employeeId = id; }

    public Long getDeptId()               { return deptId; }
    public void setDeptId(Long id)        { this.deptId = id; }

    public String getDeptName()           { return deptName; }
    public void setDeptName(String name)  { this.deptName = name; }

    public String getFullName()           { return fullName; }
    public void setFullName(String name)  { this.fullName = name; }

    public String getWorkEmail()          { return workEmail; }
    public void setWorkEmail(String mail) { this.workEmail = mail; }

    public String getPassword()           { return password; }
    public void setPassword(String pwd)   { this.password = pwd; }

    public String getGradeLevel()         { return gradeLevel; }
    public void setGradeLevel(String g)   { this.gradeLevel = g; }

    public Boolean getIsAdmin()           { return isAdmin; }
    public void setIsAdmin(Boolean a)     { this.isAdmin = a; }

    public Boolean getActive()            { return active; }
    public void setActive(Boolean a)      { this.active = a; }

    public Set<Role> getRoles()           { return roles; }
    public void setRoles(Set<Role> r)     { this.roles = r; }
}


/*package com.hrms.auth.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import org.hibernate.envers.Audited;

@Audited

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long empId;
    private Long deptId;
    private String deptName;
    private String fullName;

    @Column(unique = true, nullable = false)
    private String workEmail;

    private String password;
    private Boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    //getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEmpId() { return empId; }
    public void setEmpId(Long empId) { this.empId = empId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}*/
