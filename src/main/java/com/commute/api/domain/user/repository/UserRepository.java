package com.commute.api.domain.user.repository;

import com.commute.api.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository
 *
 * @author Administrator
 * @version 1.0.0
 * @since 2026/10/08
 */
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByUserEmail(
            @NotBlank(message = "이메일은 필수입니다.")
            @Email(message = "올바른 이메일 형식이 아닙니다.")
            String userEmail);

    Optional<User> findByUserEmail(String userEmail);

}
