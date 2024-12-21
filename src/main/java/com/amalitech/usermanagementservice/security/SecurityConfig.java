package com.amalitech.usermanagementservice.security;

import com.amalitech.usermanagementservice.config.cors.CustomCorsConfiguration;
import com.amalitech.usermanagementservice.security.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/", "/api/v1/auth/login","/api/v1/auth/password-reset", "/api/v1/users/register", "/api/v1/users/check-username","/api/v1/users/{id}", "/actuator/**", "/v2/api-docs", "/*/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/swagger-ui/index.html",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"};

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomCorsConfiguration customCorsConfiguration;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(WHITE_LIST_URL).permitAll();
                    req.anyRequest().authenticated();
                })
                .cors(c -> c.configurationSource(customCorsConfiguration))
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}
