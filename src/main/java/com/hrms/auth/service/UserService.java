package com.hrms.auth.service;

import com.hrms.auth.dto.UserDto;

import java.util.List;

public interface UserService {
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
