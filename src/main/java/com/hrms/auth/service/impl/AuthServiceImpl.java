package com.hrms.auth.service.impl;

import com.hrms.auth.dto.ChangePasswordRequest;
import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.dto.LoginResponse;
import com.hrms.auth.entity.User;
import com.hrms.auth.exception.InvalidOldPasswordException;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.security.JwtUtil;
import com.hrms.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    @Autowired private UserRepository  userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil         jwtUtil;

    /* ─────────────── LOGIN ─────────────── */

    @Override
    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByWorkEmail(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        Set<String> roleNames = user.getRoles().stream()
                                    .map(r -> r.getName())
                                    .collect(Collectors.toSet());

        Map<String,Object> claims = new HashMap<>();
        claims.put("roles", roleNames);

        // JwtUtil signature is generateToken(Map<String,?> claims, String subject)
        String token = jwtUtil.generateToken(claims, user.getWorkEmail());

        return new LoginResponse(token, user.getWorkEmail(), user.getFullName());
    }

    /* ──────── CHANGE-PASSWORD overloads ──────── */

    /** Matches interface version that uses a DTO. */
    public void changePassword(String username, ChangePasswordRequest req) {
        changePassword(username, req.getOldPassword(), req.getNewPassword());
    }

    /** Matches interface version that uses three separate strings. */
    public void changePassword(String username,
                               String currentPwd,
                               String newPwd) {

        User user = userRepo.findByWorkEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPwd, user.getPassword())) {
            throw new InvalidOldPasswordException("Current password wrong");
        }

        user.setPassword(passwordEncoder.encode(newPwd));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepo.save(user);
    }

    /* ───────── Spring Security hook ───────── */

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepo.findByWorkEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getWorkEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .flatMap(r -> r.getPermissions().stream())
                        .map(p -> new SimpleGrantedAuthority(p.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }
}






/*package com.hrms.auth.service.impl;

import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.dto.LoginResponse;
import com.hrms.auth.entity.User;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.security.JwtUtil;
import com.hrms.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByWorkEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UsernameNotFoundException("Invalid credentials");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()));
        String token = jwtUtil.generateToken(user.getWorkEmail(), claims);
        return new LoginResponse(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByWorkEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
                user.getWorkEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(perm -> new SimpleGrantedAuthority(perm.getName()))
                        .collect(Collectors.toSet())
        );
    }
}
*/