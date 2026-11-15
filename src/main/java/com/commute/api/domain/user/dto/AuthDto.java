package com.commute.api.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthDto() {

    public record AuthRequest(
            @NotBlank(message = "이메일은 필수입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String userEmail,

            @NotBlank(message = "비밀번호는 필수입니다.")
            @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
            String password,

            // 관리자 로그인(SCR-A01) 여부 (일반 로그인 시에는 null 또는 false)
            Boolean admin
    ) {}

    public record AuthResult (
            String accessToken
            ,String userEmail
            , String role
    ) {}

    public record AuthResponse(
            String userEmail
            , String role
    ) {}

}
