package com.commute.api.domain.user.controller;

import com.commute.api.domain.user.dto.AuthDto;
import com.commute.api.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * クラスの説明を記入する。
 * Write the class description.
 *
 * @author Kim Gwangjin
 * @since 2026/11/15
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.expiration}")
    private long expirationMs;

    @PostMapping("/signup")
    public ResponseEntity<AuthDto.AuthResponse> signup(@Valid @RequestBody AuthDto.AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto.AuthResponse> login(@Valid @RequestBody AuthDto.AuthRequest request) {
        boolean requireAdmin = Boolean.TRUE.equals(request.admin());
        AuthDto.AuthResult authResult = authService.login(request, requireAdmin);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createCookie(authResult.accessToken()).toString())
                .body(new AuthDto.AuthResponse(authResult.userEmail(), authResult.role()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true).path("/").maxAge(0).sameSite("Lax").build();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    private ResponseCookie createCookie (String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false)          // todo 프로퍼티로 관리
                .maxAge(expirationMs)
                .path("/")
                .sameSite("Lax")
                .build();
    }

}
