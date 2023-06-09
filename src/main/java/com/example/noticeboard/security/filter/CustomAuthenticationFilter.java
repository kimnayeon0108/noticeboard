package com.example.noticeboard.security.filter;

import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.security.dto.ReqLoginDto;
import com.example.noticeboard.security.exception.BaseAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(ObjectMapper objectMapper,
                                      AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/login", "POST"), authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken;

        try {
            ReqLoginDto loginDto = objectMapper.readValue(request.getInputStream(), ReqLoginDto.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        } catch (IOException e) {
            throw new BaseAuthenticationException(ErrorCode.INVALID_REQUEST_BODY);
        }

        return this.getAuthenticationManager()
                   .authenticate(authenticationToken);
    }
}
