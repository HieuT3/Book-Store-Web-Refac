package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.request.ResetPasswordRequest;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.dto.response.UserProfileResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.entity.PasswordResetToken;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.PasswordResetTokenRepository;
import com.bookstore.app.repository.UserRepository;
import com.bookstore.app.security.CustomerUserDetails;
import com.bookstore.app.security.JwtAuthenticationProvider;
import com.bookstore.app.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    JwtAuthenticationProvider jwtAuthenticationProvider;
    PasswordEncoder passwordEncoder;
    ModelMapper modelMapper;
    UserRepository userRepository;
    UserDetailsService userDetailsService;
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public JwtTokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

        Cookie cookie = new Cookie("refresh-token", jwtAuthenticationProvider.generateRefreshToken(userDetails));
        cookie.setMaxAge((int) jwtAuthenticationProvider.getJwtRefreshTokenExpiration());
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return JwtTokenResponse.builder()
                .accessToken(jwtAuthenticationProvider.generateAccessToken(userDetails))
                .expiration(jwtAuthenticationProvider.getJwtAccessTokenExpiration())
                .user(modelMapper.map(userDetails.getUser(), UserResponse.class))
                .build();
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setPath("/");
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public UserResponse resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: " + email + " not found"));
        passwordResetTokenRepository.findByUser(user)
                .ifPresent(passwordResetTokenRepository::delete);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public Long confirmResetPassword(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        if(passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new ResourceAlreadyExistsException("Token has expired");
        User user = passwordResetToken.getUser();
        return user.getUserId();
    }

    @Override
    public UserResponse changPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(resetPasswordRequest.getToken())
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        User user = passwordResetToken.getUser();
        if(!user.getUserId().equals(resetPasswordRequest.getUserId()))
            throw new IllegalStateException("Invalid user with token");
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public UserProfileResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        return modelMapper.map(user, UserProfileResponse.class);
    }

    @Override
    public JwtTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh-token")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }

            if (refreshToken.isEmpty())
                throw new RuntimeException("Refresh token not found");
            try {
                if (!jwtAuthenticationProvider.isTokenValid(refreshToken)) {
                    Cookie cookie = new Cookie("refresh-token", null);
                    cookie.setPath("/");
                    cookie.setSecure(false);
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(0);

                    throw new RuntimeException("Refresh token expired or invalid");
                }

                String username = jwtAuthenticationProvider.extractUserName(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                return JwtTokenResponse.builder()
                        .accessToken(jwtAuthenticationProvider.generateAccessToken(userDetails))
                        .expiration(jwtAuthenticationProvider.getJwtAccessTokenExpiration())
                        .build();
            } catch (RuntimeException e){
                throw new RuntimeException();
            }
        } else {
            throw new RuntimeException("Cookies not found");
        }
    }
}
