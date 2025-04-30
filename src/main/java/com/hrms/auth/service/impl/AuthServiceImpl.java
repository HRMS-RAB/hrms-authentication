package com.hrms.auth.service.impl;

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
