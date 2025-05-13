
// UserServiceImpl.java
// Path: hrms-authentication/src/main/java/com/hrms/auth/service/impl/UserServiceImpl.java

package com.hrms.auth.service.impl;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.mapper.UserMapper;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo,
                           RoleRepository roleRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createOrUpdateUser(UserDto dto) {
        // Validate mandatory fields
        if (!StringUtils.hasText(dto.getWorkEmail())) {
            throw new IllegalArgumentException("workEmail is mandatory");
        }

        boolean admin = dto.getRoles().contains("HR_ADMIN");
        dto.setIsAdmin(admin);

        if (!admin && dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("employeeId is mandatory for non-admin users");
        }

        // Prevent duplicate email on create
        if (dto.getUserId() == null && userRepo.existsByWorkEmail(dto.getWorkEmail())) {
            throw new IllegalArgumentException("User with email " + dto.getWorkEmail() + " already exists.");
        }

        // Resolve roles
        Set<Role> roles = dto.getRoles().stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + name + " not found")))
                .collect(Collectors.toSet());

        // Map DTO to entity
        User entity = UserMapper.toEntity(dto, roles);

        // Set default encoded password
        String defaultPassword = "Password@1";
        entity.setPassword(passwordEncoder.encode(defaultPassword));

        try {
            User saved = userRepo.save(entity);
            return UserMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("DB constraint violation", ex);
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                       .stream()
                       .map(UserMapper::toDto)
                       .collect(Collectors.toList());
    }
}


//++++++++++below this one was used++++++++++++++++
/*
package com.hrms.auth.service.impl;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.mapper.UserMapper;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public UserDto createOrUpdateUser(UserDto dto) {

        if (!StringUtils.hasText(dto.getWorkEmail())) {
            throw new IllegalArgumentException("workEmail is mandatory");
        }

        boolean admin = dto.getRoles().contains("HR_ADMIN");
        dto.setIsAdmin(admin);

        if (!admin && dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("employeeId is mandatory for non-admin users");
        }

        /* Prevent duplicate email when creating 
        if (dto.getUserId() == null && userRepo.existsByWorkEmail(dto.getWorkEmail())) {
            throw new IllegalArgumentException("User with email " + dto.getWorkEmail() + " already exists.");
        }

         Resolve Role entities 
        Set<Role> roles = dto.getRoles().stream()
                .map(name -> roleRepo.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + name + " not found")))
                .collect(Collectors.toSet());

        User entity = UserMapper.toEntity(dto, roles);

        try {
            User saved = userRepo.save(entity);
            return UserMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("DB constraint violation", ex);
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                       .stream()
                       .map(UserMapper::toDto)
                       .collect(Collectors.toList());
    }
}
*/
//++++++++++above this one was used++++++++++++++++



/*package com.hrms.auth.service.impl;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.User;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createOrUpdateUser(User user) {
        // Reject duplicate email creation
        if (user.getUserId() == null && userRepository.existsByWorkEmail(user.getWorkEmail())) {
            throw new IllegalArgumentException("User with email " + user.getWorkEmail() + " already exists.");
        }

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setWorkEmail(user.getWorkEmail());
        dto.setFullName(user.getFullName());
        dto.setRoles(user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet()));
        return dto;
    }
}
*/