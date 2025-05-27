package com.hrms.auth.repository;

import com.hrms.auth.entity.PasswordResetToken;
import com.hrms.auth.entity.User;                 //  <<< CHANGED >>>
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    @Query("""
           select t
           from   PasswordResetToken t
           join fetch t.user
           where  t.token = :token
           """)
    Optional<PasswordResetToken> findByToken(String token);

    /* ───────────── NEW — clears old tokens for a user ───────────── */  //  <<< CHANGED >>>
    void deleteByUser(User user);                                         //  <<< CHANGED >>>
}
