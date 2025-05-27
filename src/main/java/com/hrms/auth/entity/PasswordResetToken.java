package com.hrms.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String token;

    //@Column(nullable = false)
    @Column(name = "expires_at")          //  <<< CHANGED >>>
    private LocalDateTime expiry;

    @ManyToOne(fetch = FetchType.LAZY)      // keep LAZY, we fetch via JPQL
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
