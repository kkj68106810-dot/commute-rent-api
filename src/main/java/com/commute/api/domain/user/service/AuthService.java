package com.commute.api.domain.user.service;

import com.commute.api.common.security.JwtTokenProvider;
import com.commute.api.domain.user.dto.AuthDto;
import com.commute.api.domain.user.entity.User;
import com.commute.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * [KO] 회원가입: 중복 확인 및 INSERT 원자성 보장을 위해 쓰기 트랜잭션(@Transactional) 적용
     * [JA] 会員登録：重複確認およびINSERTの原子性(Atomicity)を保証するため、書き込みトランザクション(@Transactional)を適用
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    @Transactional
    public AuthDto.AuthResponse signup (AuthDto.AuthRequest auth) {
        if(userRepository.existsByUserEmail(auth.userEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .userEmail(auth.userEmail())
                .passwordHash(passwordEncoder.encode(auth.password()))
                .role("USER")
                .provider("local")
                .build();
        User savedUser = userRepository.save(user);

        return  new AuthDto.AuthResponse (savedUser.getUserEmail(), savedUser.getRole());
    }

    /**
     * [KO] 로그인: 단건 조회(findByEmail) 직후 DB 커넥션을 즉시 반납하고,
     *      CPU 연산 비용이 큰 BCrypt 해시 검증 동안 HikariCP 커넥션을 점유하지 않도록 @Transactional 생략
     * [JA] ログイン：単一検索(findByEmail)の直後にDBコネクションを即時返却し、
     *      CPU負荷の高いBCryptハッシュ検証中にHikariCPコネクションを占有しないよう@Transactionalを省略
     *
     * @param name 説明 / Description
     * @return 戻り値の説明 / Return value description
     */
    public AuthDto.AuthResult login(AuthDto.AuthRequest request, boolean requireAdmin) {
        User user = userRepository.findByUserEmail(request.userEmail())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "이메일 또는 비밀번호 불일치요"));

        // todo 예외처리 점검요망
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED,
                    "이메일 또는 비밀번호가 올바르지 않습니다. / 認証に失敗しました。");
        }

        String token = jwtTokenProvider.createToken(user.getId(), user.getUserEmail(), user.getRole());
        return new AuthDto.AuthResult(token, user.getUserEmail(), user.getRole());
    }

}
