package com.hrms.auth.service;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.listener.EmployeeEvent;

import java.util.List;

public interface UserService {

    /** Create or update a User based on an EmployeeEvent from hrms-backend. */
    UserDto upsertFromEmployeeEvent(EmployeeEvent evt);

    /** Direct CRUD (used by admin APIs) */
    UserDto createOrUpdateUser(UserDto dto);

    List<UserDto> getAllUsers();
}






/*package com.hrms.auth.service;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.User;

import java.util.List;

public interface UserService {
    UserDto createOrUpdateUser(User user);
    List<UserDto> getAllUsers();
}*/
