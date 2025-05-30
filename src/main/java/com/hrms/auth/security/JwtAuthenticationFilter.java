// src/main/java/com/hrms/auth/security/JwtAuthenticationFilter.java

package com.hrms.auth.security;

import com.hrms.auth.entity.User;
import com.hrms.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired private JwtUtil        jwtUtil;
    @Autowired private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String hdr = req.getHeader("Authorization");
        if (hdr == null || !hdr.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = hdr.substring(7);
        String email = null;
        try {
            email = jwtUtil.extractClaim(token, Claims::getSubject);
        } catch (Exception ignored) {
            // malformed token – let validation handle it
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepo.findByWorkEmail(email).orElse(null);
            if (user != null) {

                boolean valid = jwtUtil.validateToken(
                        token, user.getWorkEmail(), user.getPasswordChangedAt());

                if (valid) {
                    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                            .flatMap(r -> r.getPermissions().stream())
                            .map(p -> new SimpleGrantedAuthority(p.getName()))
                            .collect(Collectors.toList());

                    UserDetails details = org.springframework.security.core.userdetails.User
                            .withUsername(user.getWorkEmail())
                            .password(user.getPassword())   // not checked here
                            .authorities(authorities)
                            .build();

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    details, null, details.getAuthorities());
                    auth.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    log.info("JWT rejected [{}] – reason={}  iat={}  pwdChangedAt={}",
                             email,
                             jwtUtil.getLastError(),
                             jwtUtil.extractClaim(token, Claims::getIssuedAt),
                             user.getPasswordChangedAt());
                }
            }
        }
        chain.doFilter(req, res);
    }
}











/*package com.hrms.auth.security;

import com.hrms.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//** Extracts “Bearer …” header, verifies JWT, and populates the security context. 
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil     jwtUtil;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String header   = request.getHeader(HttpHeaders.AUTHORIZATION);
        String       token    = null;
        String       username = null;

        if (header != null && header.startsWith("Bearer ")) {
            token    = header.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = authService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

*/

