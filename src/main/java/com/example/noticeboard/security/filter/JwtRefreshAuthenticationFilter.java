package com.example.noticeboard.security.filter;

import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.security.dto.ReqIssueJwtDto;
import com.example.noticeboard.security.exception.BaseAuthenticationException;
import com.example.noticeboard.security.token.JwtAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

public class JwtRefreshAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public JwtRefreshAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
                                          AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        JwtAuthenticationToken authenticationToken;

        try {
            ReqIssueJwtDto issueJwtDto = objectMapper.readValue(request.getInputStream(), ReqIssueJwtDto.class);
            if (!issueJwtDto.getGrantType()
                            .equals("refresh_token")) {
                throw new BaseAuthenticationException(ErrorCode.INVALID_TOKEN);
            }

            authenticationToken = new JwtAuthenticationToken(issueJwtDto.getRefreshToken(), null);

        } catch (IOException e) {
            throw new BaseAuthenticationException(ErrorCode.INVALID_REQUEST_BODY);
        }

        return this.getAuthenticationManager()
                   .authenticate(authenticationToken);
    }
}
