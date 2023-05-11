package com.example.noticeboard.service;

import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.request.ReqSignupDto;
import com.example.noticeboard.dto.response.ResUserDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.UserRepository;
import com.example.noticeboard.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ResUserDto signup(ReqSignupDto reqSignupDto) {

        if (userRepository.existsByEmail(reqSignupDto.getEmail())) {
            throw new BaseException(ErrorCode.ALREADY_SIGNUP);
        }

        String encodedPassword = passwordEncoder.encode(reqSignupDto.getPassword());
        User user = User.builder()
                        .email(reqSignupDto.getEmail())
                        .name(reqSignupDto.getName())
                        .encryptedPassword(encodedPassword)
                        .role(UserRole.USER)
                        .build();

        userRepository.save(user);

        return ResUserDto.of(user);
    }
}
