package com.example.noticeboard.security.config.filter;

import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.security.config.dto.ReqLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authenticationToken;

        try {
            ReqLoginDto loginDto = objectMapper.readValue(request.getInputStream(), ReqLoginDto.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        } catch (IOException e) {
            throw new BaseException(ErrorCode.INVALID_INPUT);
        }
        setDetails(request, authenticationToken);

        return this.getAuthenticationManager()
                   .authenticate(authenticationToken);
    }
}
