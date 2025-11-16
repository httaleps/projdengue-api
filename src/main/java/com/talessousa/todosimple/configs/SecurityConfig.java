package com.talessousa.todosimple.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.talessousa.todosimple.security.JWTAuthenticationFilter;
import com.talessousa.todosimple.security.JWTAuthorizationFilter;
import com.talessousa.todosimple.security.JWTUtil;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] PUBLIC_MATCHERS = { "/" };
    private static final String[] PUBLIC_MATCHERS_POST = { "/pessoa", "/usuario", "/login" };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            JWTUtil jwtUtil,
            org.springframework.security.core.userdetails.UserDetailsService userDetailsService
            ) throws Exception {

        JWTAuthenticationFilter jwtAuthFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtil);
        JWTAuthorizationFilter jwtAuthzFilter = new JWTAuthorizationFilter(jwtUtil, userDetailsService);

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )

            .exceptionHandling(exception -> exception
                // Cenário 1: Usuário LOGADO, mas sem permissão (Access Denied)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\": 403, \"message\": \"Acesso negado.\"}");
                })
                // Cenário 2: Usuário ANÔNIMO ou Token Inválido (Authentication Entry Point)
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"status\": 403, \"message\": \"Acesso negado.\"}");
                })
            )
            // ------------------------------------------
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthzFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}