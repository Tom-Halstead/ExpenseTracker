package com.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**", "/users/register").permitAll()  // Public endpoints
                                .anyRequest().authenticated()  // Protect all other endpoints
                )
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF if using JWT or for testing only
                .oauth2Login(oauth2 ->
                        oauth2
//                                .loginPage("/auth/login")  // Specify custom login page
                                .defaultSuccessUrl("/home", true)  // Redirect after successful login
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}