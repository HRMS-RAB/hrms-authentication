
package com.hrms.auth.controller;

import com.hrms.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@RequestBody Map<String, String> body) {
        // Accept “workEmail” or fallback to “email”
        String email = body.getOrDefault("workEmail", body.get("email"));
        log.info("► Received forgot-password request for [{}]", email);
        passwordResetService.forgot(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestBody Map<String,String> body) {
        String token  = body.get("token");
        String newPwd = body.get("newPwd");
        log.info("► Received confirm-password request for token [{}]", token);
        passwordResetService.confirm(token, newPwd);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change")
    public ResponseEntity<Void> change(@RequestBody Map<String,String> body,
                                       Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }
        log.info("► Received change-password request for user [{}]", auth.getName());
        passwordResetService.change(
            auth.getName(),
            body.get("currentPwd"),
            body.get("newPwd"));
        return ResponseEntity.ok().build();
    }
}





///working code below 
/*package com.hrms.auth.controller;

import com.hrms.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// All routes live under /auth/password/**
 
@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

 //    ─────────────────────────── PUBLIC ─────────────────────────── 

    @PostMapping("/forgot")
    public ResponseEntity<Void> forgot(@RequestBody Map<String, String> body) {
        passwordResetService.forgot(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestBody Map<String, String> body) {
        passwordResetService.confirm(body.get("token"), body.get("newPwd"));
        return ResponseEntity.ok().build();
    }

    // ──────────────────────── AUTHENTICATED ─────────────────────── 

    @PostMapping("/change")
    public ResponseEntity<Void> change(@RequestBody Map<String, String> body,
                                       Authentication auth) {
        passwordResetService.change(
                auth.getName(),
                body.get("currentPwd"),
                body.get("newPwd"));
        return ResponseEntity.ok().build();
    }
}
*/