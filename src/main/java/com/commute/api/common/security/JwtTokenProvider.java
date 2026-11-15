package com.commute.api.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long validityInMilliseconds;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret
            , @Value("${jwt.expiration}") long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    /**
     * 処理内容を記入する。
     * Write what this method does.
     *
     * @param userId, userEmail, role   説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    public String createToken(long userId, String userEmail, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(String.valueOf(userId)) // PK(user_id)를 subject에 저장
                .claim("email", userEmail)
                .claim("role", role)             // "USER" 또는 "ADMIN"
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)             // 서버 비밀키로 위조 방지 서명
                .compact();
    }

    /**
     * 処理内容を記入する。
     * Write what this method does.
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Long userId = Long.valueOf(claims.getSubject());
        String role = claims.get("role", String.class);

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(userId, "", authorities);
    }


}
