package com.commute.api.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "commute_rent", uniqueConstraints = {@UniqueConstraint(name = "uq_users_email",
        columnNames = {"user_email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Size(max = 255)
    @Column(name = "password_hash")
    private String passwordHash;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'USER'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;


    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'local'")
    @Column(name = "provider", length = 20)
    private String provider = "local";

    @Size(max = 255)
    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Builder
    public User(String userEmail, String passwordHash, String role, String provider) {
        this.userEmail = userEmail;
        this.passwordHash = passwordHash;
        this.role = role;
//        this.provider = provider;
    }


    public User() {

    }
}