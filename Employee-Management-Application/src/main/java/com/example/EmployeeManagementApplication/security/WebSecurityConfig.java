package com.example.EmployeeManagementApplication.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //noinspection removal
        http

                //.cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin().disable()
                .securityMatcher("/api/v1/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests// URL to be secured
                                .requestMatchers("/api/v1/register").permitAll()
                                .requestMatchers("/api/v1/login").permitAll()
                                .requestMatchers("/api/v1/employee/**").permitAll()
                                .requestMatchers("/api/v1/project/**").permitAll()
                                .requestMatchers("/api/v1/get_employee/**").hasRole("USER")
                                .requestMatchers("/api/v1/get_project/**").hasRole("USER")
                                .anyRequest().authenticated()
                                .and().authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)// Allow all other requests without authentication
                );

        return http.build();
    }
}
