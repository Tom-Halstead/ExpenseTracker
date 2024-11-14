package com.expensetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/public/**").permitAll()  // Public paths
//                                .anyRequest().authenticated()  // Secure all other paths
//                )
//                .formLogin(form -> form
//                        .loginPage("/login")  // If you want to customize the login page
//                        .permitAll()
//                        .defaultSuccessUrl("/", true)  // Redirect after successful login
//                )
//                .logout(logout -> logout.permitAll());  // Allow logout for all users
//
        return http.build();
    }
}
