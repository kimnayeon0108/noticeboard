package com.example.noticeboard.security.handler;

import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.security.dto.ResTokenDto;
import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = ((UserDetailsDto) authentication.getPrincipal()).getUser();
        String token = jwtProvider.generateJwtToken(user);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter()
                .write(objectMapper.writeValueAsString(ResponseDto.success(new ResTokenDto(token))));
    }
}
