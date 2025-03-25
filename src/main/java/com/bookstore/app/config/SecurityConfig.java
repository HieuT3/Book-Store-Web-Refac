package com.bookstore.app.config;

import com.bookstore.app.filter.JwtAuthenticationFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    JwtAuthenticationFilter jwtAuthenticationFilter;
    AuthenticationEntryPoint authenticationEntryPoint;
    AccessDeniedHandler accessDeniedHandler;
    String[] PUBLIC_ENDPOINT  = {
      "/api/v1/auth/login",
      "/api/v1/auth/reset-password",
      "/api/v1/auth/confirm-reset-password",
      "/api/v1/auth/refresh-token",
      "/api/v1/cart/**",
      "/api/v1/register/**",
      "/api/v1/search",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(PUBLIC_ENDPOINT).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/author/**").permitAll()
                                .requestMatchers("/api/v1/author/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/book/**").permitAll()
                                .requestMatchers("/api/v1/book/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/v1/book-document/**").permitAll()
                                .requestMatchers("/api/v1/book-document/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/category/**").permitAll()
                                .requestMatchers("/api/v1/category/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/cloudinary/**").permitAll()
                                .requestMatchers("/api/v1/cloudinary/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/order-detail/**").permitAll()
                                .requestMatchers("/api/v1/order-detail/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/order/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/review/**").authenticated()
                                .requestMatchers("/api/v1/user/**").hasRole("ADMIN")
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
