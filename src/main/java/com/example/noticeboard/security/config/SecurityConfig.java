package com.example.noticeboard.security.config;

import com.example.noticeboard.security.filter.CustomAuthenticationFilter;
import com.example.noticeboard.security.filter.JwtAuthenticationFilter;
import com.example.noticeboard.security.handler.CustomAuthenticationFailureHandler;
import com.example.noticeboard.security.handler.CustomAuthenticationSuccessHandler;
import com.example.noticeboard.security.jwt.JwtDecoder;
import com.example.noticeboard.security.jwt.JwtProvider;
import com.example.noticeboard.security.jwt.TokenExtractor;
import com.example.noticeboard.security.provider.CustomAuthenticationProvider;
import com.example.noticeboard.security.provider.JwtAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final TokenExtractor tokenExtractor;
    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin()
            .disable()
            .httpBasic()
            .disable();

        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests()
            .antMatchers("/categories/**")
            .hasRole("ADMIN")
            .anyRequest()
            .permitAll();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(objectMapper, authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
        authenticationFilter.afterPropertiesSet();
        return authenticationFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {

        JwtAuthenticationFilter authenticationFilter =
                new JwtAuthenticationFilter(new OrRequestMatcher(
                        new AntPathRequestMatcher("/categories/**"),
                        new AntPathRequestMatcher("/posts", "POST"),
                        new AntPathRequestMatcher("/posts", "PUT"),
                        new AntPathRequestMatcher("/posts", "DELETE"),
                        new AntPathRequestMatcher("/user/**")
                ),
                        authenticationManager(),
                        tokenExtractor);
        return authenticationFilter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtDecoder);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider(), jwtAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(jwtProvider, objectMapper);
    }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(objectMapper);
    }
}
