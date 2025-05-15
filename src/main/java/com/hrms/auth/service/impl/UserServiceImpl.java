package com.hrms.auth.service.impl;

import com.hrms.auth.dto.UserDto;
import com.hrms.auth.entity.Role;
import com.hrms.auth.entity.User;
import com.hrms.auth.listener.EmployeeEvent;
import com.hrms.auth.mapper.UserMapper;
import com.hrms.auth.repository.RoleRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

    /*──────────────────── event-driven upsert ────────────────────*/

    @Override
    public UserDto upsertFromEmployeeEvent(EmployeeEvent evt) {

        // build a DTO from the event
        UserDto dto = new UserDto();
        dto.setEmployeeId(evt.getEmployeeId());
        dto.setWorkEmail(evt.getWorkEmail());
        dto.setFullName(evt.getFullName());
        dto.setGradeLevel(evt.getGradeLevel());
        dto.setRoles(Set.of("EMPLOYEE"));      // default role; RoleAssignmentService will refine

        return createOrUpdateUser(dto);
    }

    /*──────────────────── admin / API CRUD ───────────────────────*/

    @Override
    public UserDto createOrUpdateUser(UserDto dto) {

        if (!StringUtils.hasText(dto.getWorkEmail())) {
            throw new IllegalArgumentException("workEmail is mandatory");
        }

        boolean admin = dto.getRoles() != null && dto.getRoles().contains("HR_ADMIN");
        dto.setIsAdmin(admin);

        if (!admin && dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("employeeId is mandatory for non-admin users");
        }

        // Prevent duplicate email on CREATE
        if (dto.getUserId() == null && userRepo.existsByWorkEmail(dto.getWorkEmail())) {
            throw new IllegalArgumentException("User with email " + dto.getWorkEmail() + " already exists.");
        }

        // Resolve Role entities
        Set<Role> roles = dto.getRoles() == null ? Set.of() :
                dto.getRoles().stream()
                        .map(name -> roleRepo.findByName(name)
                                .orElseThrow(() -> new IllegalArgumentException("Role " + name + " not found")))
                        .collect(Collectors.toSet());

        // Map DTO -> Entity
        User entity = UserMapper.toEntity(dto, roles);

        // Ensure password (default for new users)
        if (entity.getPassword() == null) {
            String defaultPwd = passwordEncoder.encode("Welcome@123");
            entity.setPassword(defaultPwd);
        }

        try {
            User saved = userRepo.save(entity);
            return UserMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            log.error("DB constraint violation saving user {}", dto.getWorkEmail(), ex);
            throw new IllegalStateException("Unable to save user", ex);
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