package com.hrms.auth.service;

import com.hrms.auth.entity.User;
import com.hrms.auth.listener.EmployeeEvent;

public interface RoleAssignmentService {
   
	void assignRole(EmployeeEvent event);
	
	// void assignRole(User user, EmployeeEvent event);
}
