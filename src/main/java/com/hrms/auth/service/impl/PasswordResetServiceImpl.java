package com.hrms.auth.service.impl;

import com.hrms.auth.entity.PasswordResetToken;
import com.hrms.auth.entity.User;
import com.hrms.auth.exception.InvalidTokenException;
import com.hrms.auth.repository.PasswordResetTokenRepository;
import com.hrms.auth.repository.UserRepository;
import com.hrms.auth.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository               userRepo;
    private final PasswordEncoder              encoder;

    /* ───────────── forgot ───────────── */

    @Override
    @Transactional
    public void forgot(String workEmail) {                            //  <<< CHANGED >>>

        userRepo.findByWorkEmail(workEmail).ifPresent(user -> {      //  <<< CHANGED >>>
            tokenRepo.deleteByUser(user);

            String token = UUID.randomUUID().toString();
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(token);
            prt.setExpiry(LocalDateTime.now().plusMinutes(30));
            prt.setUser(user);
            tokenRepo.save(prt);

            log.info("Password-reset token for {} is {}", workEmail, token);
        });
    }

    /* ───────────── confirm ───────────── */

    @Override
    @Transactional                                    // keeps session open
    public void confirm(String token, String newPwd) {

        PasswordResetToken prt = tokenRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));

        if (prt.getExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token expired");
        }

        User user = prt.getUser();                      // already initialised
        user.setPassword(encoder.encode(newPwd));
        user.setPasswordChangedAt(LocalDateTime.now()); //  <<< CHANGED >>>

        userRepo.save(user);
        tokenRepo.delete(prt);
    }

    /* ───────────── change ───────────── */

    @Override
    @Transactional
    public void change(String workEmail, String currentPwd, String newPwd) { //  <<< CHANGED >>>

        User user = userRepo.findByWorkEmail(workEmail)                      //  <<< CHANGED >>>
                .orElseThrow(() -> new InvalidTokenException("User not found"));

        if (!encoder.matches(currentPwd, user.getPassword())) {
            throw new InvalidTokenException("Current password incorrect");
        }

        user.setPassword(encoder.encode(newPwd));
        user.setPasswordChangedAt(LocalDateTime.now());       //  <<< CHANGED >>>
        userRepo.save(user);
    }
}
