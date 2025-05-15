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

        /* ── basic field checks ── */
        if (!StringUtils.hasText(dto.getWorkEmail())) {
            throw new IllegalArgumentException("workEmail is mandatory");
        }
        boolean admin = dto.getRoles() != null && dto.getRoles().contains("HR_ADMIN");
        dto.setIsAdmin(admin);

        if (!admin && dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("employeeId is mandatory for non-admin users");
        }

        /* ── TRUE UPSERT ────────────────────────────────────────────── */
        User entity = null;

        // 1) look up by email
        entity = userRepo.findByWorkEmail(dto.getWorkEmail()).orElse(null);

        // 2) if not found, look up by employeeId
        if (entity == null && dto.getEmployeeId() != null) {
            entity = userRepo.findByEmployeeId(dto.getEmployeeId()).orElse(null);
            if (entity != null                     // same employee found
                && !entity.getWorkEmail().equalsIgnoreCase(dto.getWorkEmail())) {

                // email mismatch → fail fast so real data error is visible
                throw new IllegalStateException("EmployeeId " + dto.getEmployeeId()
                        + " already linked to email " + entity.getWorkEmail()
                        + " — event email " + dto.getWorkEmail() + " rejected");
            }
        }

        /* ── create if brand-new ── */
        if (entity == null) {
            entity = new User();
            entity.setPassword(passwordEncoder.encode("Welcome@123"));
        }

        /* ── map incoming DTO fields ── */
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setWorkEmail (dto.getWorkEmail());
        entity.setFullName  (dto.getFullName());
        entity.setGradeLevel(dto.getGradeLevel());
        entity.setDeptId(dto.getDeptId());
        entity.setDeptName(dto.getDeptName());
        entity.setIsAdmin(dto.getIsAdmin());
        entity.setActive(dto.getActive() == null || dto.getActive());

        /* ── resolve & attach roles ── */
        Set<Role> roles = dto.getRoles() == null ? Set.of() :
                dto.getRoles().stream()
                   .map(name -> roleRepo.findByName(name)
                       .orElseThrow(() -> new IllegalArgumentException("Role " + name + " not found")))
                   .collect(Collectors.toSet());
        entity.setRoles(roles);

        /* ── save ── */
        try {
            User saved = userRepo.save(entity);
            return UserMapper.toDto(saved);
        } catch (DataIntegrityViolationException ex) {
            log.error("DB constraint violation saving user {}", dto.getWorkEmail(), ex);
            throw new IllegalStateException("Unable to save user", ex);
        }
    }

    
    
    /* @Override
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
    }*/

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll()
                       .stream()
                       .map(UserMapper::toDto)
                       .collect(Collectors.toList());
    }
}

