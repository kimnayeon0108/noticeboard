package com.example.noticeboard.repository;

import com.example.noticeboard.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
