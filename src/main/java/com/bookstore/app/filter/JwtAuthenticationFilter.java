package com.bookstore.app.filter;

import com.bookstore.app.security.JwtAuthenticationProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtAuthenticationProvider jwtAuthenticationProvider;
    UserDetailsService userDetailsService;
    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request received: {}", request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.contains("Bearer")) {
            log.info("Request does not contain Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("Request contains Authorization header");
            String jwtToken = authHeader.substring(7);
            String userEmail = jwtAuthenticationProvider.extractUserName(jwtToken);
            System.out.println("test");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(userEmail != null && authentication == null) {
                log.info("User email is not null and authentication is null");
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if(jwtAuthenticationProvider.isTokenValid(jwtToken, userDetails)) {
                    log.info("Token is valid");
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("User authenticated");
                } else {
                    log.warn("Invalid JWT token");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Invalid or expired JWT token");
        }
    }
}
