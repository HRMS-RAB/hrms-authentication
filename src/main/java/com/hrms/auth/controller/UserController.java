package com.hrms.auth.controller;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('manage_users')")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
