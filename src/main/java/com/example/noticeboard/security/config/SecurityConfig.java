package com.example.noticeboard.security.config;

import com.example.noticeboard.security.filter.CustomAuthenticationFilter;
import com.example.noticeboard.security.filter.JwtAuthenticationFilter;
import com.example.noticeboard.security.filter.JwtRefreshAuthenticationFilter;
import com.example.noticeboard.security.handler.CustomAuthenticationFailureHandler;
import com.example.noticeboard.security.handler.CustomAuthenticationSuccessHandler;
import com.example.noticeboard.security.jwt.TokenExtractor;
import com.example.noticeboard.security.provider.CustomAuthenticationProvider;
import com.example.noticeboard.security.provider.JwtAuthenticationProvider;
import com.example.noticeboard.security.type.JwtAuthenticationRequestMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final TokenExtractor tokenExtractor;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfConfigurer -> csrfConfigurer.disable())
            .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(formLoginConfigurer -> formLoginConfigurer.disable())
            .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())
            .logout(logoutConfigurer -> logoutConfigurer.disable());

        http.addFilterBefore(jwtRefreshAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers("/categories/**")
                                                                                 .hasRole("ADMIN")
                                                                                 .anyRequest()
                                                                                 .permitAll());

        return http.build();
    }

    private CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(objectMapper,
                authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return authenticationFilter;
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() {

        JwtAuthenticationFilter authenticationFilter =
                new JwtAuthenticationFilter(
                        new OrRequestMatcher(JwtAuthenticationRequestMatcher.getMatchers()),
                        authenticationManager(),
                        tokenExtractor);

        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        return authenticationFilter;
    }

    private JwtRefreshAuthenticationFilter jwtRefreshAuthenticationFilter() {

        AntPathRequestMatcher requestMatcher =
                new AntPathRequestMatcher(JwtAuthenticationRequestMatcher.REFRESH_TOKEN_PATH_POST.getUrl(),
                        JwtAuthenticationRequestMatcher.REFRESH_TOKEN_PATH_POST.getMethod()
                                                                               .name());

        JwtRefreshAuthenticationFilter jwtRefreshAuthenticationFilter =
                new JwtRefreshAuthenticationFilter(requestMatcher, authenticationManager(), objectMapper);

        jwtRefreshAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        jwtRefreshAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        return jwtRefreshAuthenticationFilter;
    }

    private AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider, jwtAuthenticationProvider);
    }
}
