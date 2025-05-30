
package com.hrms.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.*;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key;
    private String lastError = "";

    public String getLastError() { return lastError; }

    public JwtUtil(@Value("${hrms.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /* ---------- issue ---------- */

    public String generateToken(Map<String, ?> claims, String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))   // 1 h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* ---------- validate ---------- */

    public boolean validateToken(String token,
                                 String expectedUsername,
                                 LocalDateTime pwdChangedAt) {

        lastError = "";

        try {
            String username = extractClaim(token, Claims::getSubject);
            if (!expectedUsername.equals(username)) {
                lastError = "subject_mismatch";
                return false;
            }

            if (isExpired(token)) {
                lastError = "expired";
                return false;
            }

            Date issuedAt = extractClaim(token, Claims::getIssuedAt);

            /* ---- Fixed timezone handling ---- */
            Date pwdChangedDate =
                Date.from(pwdChangedAt.atZone(ZoneId.systemDefault()).toInstant());

            if (issuedAt.before(pwdChangedDate)) {
                lastError = "issued_before_pwd_change";
                return false;
            }

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            lastError = "signature";
            return false;
        }
    }

    /* ---------- helpers ---------- */

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(Jwts.parserBuilder()
                                  .setSigningKey(key)
                                  .build()
                                  .parseClaimsJws(token)
                                  .getBody());
    }

    private boolean isExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }
}









/*package com.hrms.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${hrms.jwt.secret}")
    private String secret;

    @Value("${hrms.jwt.expiration}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public boolean validateToken(String token, String username) {
        final String extracted = extractUsername(token);
        return (extracted.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
*/