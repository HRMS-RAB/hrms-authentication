-- V2__seed_roles_permissions.sql
-- Insert permissions
INSERT INTO permissions(name, description) VALUES ('view_records', 'View employee records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_records', 'Create or update employee records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('delete_records', 'Delete employee records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('approve_records', 'Approve changes to employee records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_departments', 'View department details') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_departments', 'Create or update departments') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('delete_departments', 'Delete departments') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_job_requisitions', 'View job requisitions') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_job_requisitions', 'Create or update job requisitions') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('approve_job_requisitions', 'Approve job requisitions') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_candidates', 'View candidate profiles') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_candidates', 'Create or update candidate records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('delete_candidates', 'Delete candidate records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_payroll', 'View payroll data') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_payroll', 'Create or update payroll records') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('approve_payroll', 'Approve payroll runs') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_reports', 'Access HR reports') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('edit_reports', 'Generate or modify reports') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('export_data', 'Export HR data') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('manage_users', 'Manage user accounts') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('manage_roles', 'Manage roles and permissions') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('manage_configurations', 'Modify system settings') ON CONFLICT DO NOTHING;
INSERT INTO permissions(name, description) VALUES ('view_audit_logs', 'View audit logs') ON CONFLICT DO NOTHING;

-- Insert roles
INSERT INTO roles(name, description) VALUES ('HR_ADMIN', 'Full HR administrator') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('HR_MANAGER', 'HR Manager role') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('DEPT_HEAD', 'Department Head role') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('RECRUITER', 'Recruiter role') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('EMPLOYEE', 'Regular Employee role') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('PAYROLL_SPECIALIST', 'Payroll Specialist') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('COMPLIANCE_OFFICER', 'Compliance Officer') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('IT_ADMIN', 'IT Administrator') ON CONFLICT DO NOTHING;
INSERT INTO roles(name, description) VALUES ('GUEST_CONTRACTOR', 'Guest or Contractor') ON CONFLICT DO NOTHING;

-- Map roles to permissions
-- HR_ADMIN
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='delete_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='approve_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_departments'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_departments'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='delete_departments'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='approve_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='delete_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='approve_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='edit_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='export_data'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='manage_users'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='manage_roles'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='manage_configurations'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_ADMIN' AND p.name='view_audit_logs'
 ON CONFLICT DO NOTHING;
-- HR_MANAGER
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='edit_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='approve_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_departments'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='approve_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='edit_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='approve_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='view_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='edit_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='HR_MANAGER' AND p.name='export_data'
 ON CONFLICT DO NOTHING;
-- DEPT_HEAD
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='view_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='approve_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='view_departments'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='view_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='edit_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='approve_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='DEPT_HEAD' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
-- RECRUITER
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='RECRUITER' AND p.name='view_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='RECRUITER' AND p.name='edit_job_requisitions'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='RECRUITER' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='RECRUITER' AND p.name='edit_candidates'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='RECRUITER' AND p.name='delete_candidates'
 ON CONFLICT DO NOTHING;
-- EMPLOYEE
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='EMPLOYEE' AND p.name='view_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='EMPLOYEE' AND p.name='view_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='EMPLOYEE' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
-- PAYROLL_SPECIALIST
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='PAYROLL_SPECIALIST' AND p.name='view_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='PAYROLL_SPECIALIST' AND p.name='edit_payroll'
 ON CONFLICT DO NOTHING;
-- COMPLIANCE_OFFICER
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='view_records'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='view_payroll'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='view_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='edit_reports'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='export_data'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='COMPLIANCE_OFFICER' AND p.name='view_audit_logs'
 ON CONFLICT DO NOTHING;
-- IT_ADMIN
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='IT_ADMIN' AND p.name='manage_users'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='IT_ADMIN' AND p.name='manage_roles'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='IT_ADMIN' AND p.name='manage_configurations'
 ON CONFLICT DO NOTHING;
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='IT_ADMIN' AND p.name='view_audit_logs'
 ON CONFLICT DO NOTHING;
-- GUEST_CONTRACTOR
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
 WHERE r.name='GUEST_CONTRACTOR' AND p.name='view_candidates'
 ON CONFLICT DO NOTHING;
