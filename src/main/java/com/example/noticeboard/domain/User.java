package com.example.noticeboard.domain;

import com.example.noticeboard.type.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String encryptedPassword, String name, UserRole role) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.role = role;
        this.isDeleted = false;
    }

    public boolean isWriter(long userId) {
        return this.id == userId;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ROLE_ADMIN;
    }

    public void withdraw() {
        this.isDeleted = true;
    }
}
