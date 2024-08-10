package com.product.datajpa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors().disable()
                .csrf().disable()
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.POST,"/add").permitAll()
                                .requestMatchers(HttpMethod.GET, "/search").permitAll()
                                .requestMatchers(HttpMethod.GET, "/add").denyAll()
                                        .anyRequest().permitAll()
                );
//        httpSecurity.authorizeHttpRequests(authz -> authz
//                .anyRequest().permitAll()
//        )
//                .csrf().disable()
//                .cors().disable();

        return httpSecurity.build();
    }
}
