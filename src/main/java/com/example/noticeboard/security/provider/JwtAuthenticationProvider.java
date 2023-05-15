package com.example.noticeboard.security.provider;

import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.security.jwt.JwtDecoder;
import com.example.noticeboard.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        String token = (String) authenticationToken.getPrincipal();     // JWT

        if (!jwtDecoder.isValid(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        UserDetailsDto userDetailsDto = jwtDecoder.decode(token);

        return new UsernamePasswordAuthenticationToken(userDetailsDto, null,
                userDetailsDto.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
