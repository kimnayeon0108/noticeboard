package com.example.noticeboard.security.config;

import com.example.noticeboard.security.config.filter.CustomAuthenticationFilter;
import com.example.noticeboard.security.config.handler.CustomAuthenticationFailureHandler;
import com.example.noticeboard.security.config.handler.CustomAuthenticationSuccessHandler;
import com.example.noticeboard.security.config.jwt.JwtTokenProvider;
import com.example.noticeboard.security.config.provider.CustomAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable();
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin()
            .disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(objectMapper, authenticationManager());
        authenticationFilter.setFilterProcessesUrl("/user/login");
        authenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
        authenticationFilter.afterPropertiesSet();
        return authenticationFilter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(jwtTokenProvider(), objectMapper);
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }
}
