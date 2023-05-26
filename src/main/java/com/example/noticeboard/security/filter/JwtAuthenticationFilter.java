package com.example.noticeboard.security.filter;

import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.security.exception.BaseAuthenticationException;
import com.example.noticeboard.security.jwt.TokenExtractor;
import com.example.noticeboard.security.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

@Log4j2
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenExtractor tokenExtractor;

    public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager,
                                   TokenExtractor tokenExtractor) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String header = request.getHeader(TokenExtractor.AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(header)) {
            log.error("Token is empty");

            throw new BaseAuthenticationException(ErrorCode.TOKEN_NOT_FOUND);
        }

        String token = tokenExtractor.getTokenFromHeader(header);

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token, null);

        return this.getAuthenticationManager()
                   .authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }
}
