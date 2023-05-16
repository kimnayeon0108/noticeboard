package com.example.noticeboard.domain;

import com.example.noticeboard.type.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String encryptedPassword;
    private String name;
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isWriter(long userId) {
        return this.id == userId;
    }

    @Builder
    public User(String email, String encryptedPassword, String name, UserRole role) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.role = role;
        this.isDeleted = false;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ROLE_ADMIN;
    }
}
