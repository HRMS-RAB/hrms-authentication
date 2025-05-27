package com.hrms.auth.controller;

import com.hrms.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * All routes live under /auth/password/**
 */
@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;

    /* ─────────────────────────── PUBLIC ─────────────────────────── */

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

    /* ──────────────────────── AUTHENTICATED ─────────────────────── */

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
