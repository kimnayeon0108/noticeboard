package com.example.noticeboard.security.config.service;

import com.example.noticeboard.repository.UserRepository;
import com.example.noticeboard.security.config.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        return userRepository.findByEmail(email)
                             .map(user -> {
                                 SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole()
                                                                                                          .name());
                                 return new UserDetailsDto(user, Collections.singleton(grantedAuthority));
                             })
                             .orElseThrow(() -> new UsernameNotFoundException(email + " Invalid email"));
    }
}
