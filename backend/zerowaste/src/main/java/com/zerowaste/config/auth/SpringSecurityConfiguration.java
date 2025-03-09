package com.zerowaste.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zerowaste.services.users.AuthenticateUserService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    @Autowired
    private AuthenticateUserService authenticationUserService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
            
            corsConfiguration.setAllowedOrigins(java.util.List.of("*"));
            corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(java.util.List.of("*"));

            return corsConfiguration;
        }))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            //Users
            .requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/users", "/users/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/check-auth-token").hasRole("USER")
            //Products
            .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/products", "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
            //Promotions
            .requestMatchers(HttpMethod.POST, "/promotions/").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/promotions/", "/promotions/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/promotions/percentage").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/promotions/productsIds").hasAnyRole("USER", "ADMIN")
        )
        .addFilterBefore(authenticationUserService, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
