package com.example.noticeboard.security.config.handler;

import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.security.config.jwt.JwtTokenProvider;
import com.example.noticeboard.security.config.dto.ResTokenDto;
import com.example.noticeboard.security.config.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = ((UserDetailsDto) authentication.getPrincipal()).getUser();
        String token = jwtTokenProvider.generateJwtToken(user);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter()
                .write(objectMapper.writeValueAsString(ResponseDto.success(new ResTokenDto(token))));
    }
}
