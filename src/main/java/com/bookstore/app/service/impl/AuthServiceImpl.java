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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public JwtTokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
        return JwtTokenResponse.builder()
                .accessToken(jwtAuthenticationProvider.generateAccessToken(userDetails))
                .accessTokenExpiration(jwtAuthenticationProvider.getJwtAccessTokenExpiration())
                .refreshToken(jwtAuthenticationProvider.generateRefreshToken(userDetails))
                .refreshTokenExpiration(jwtAuthenticationProvider.getJwtRefreshTokenExpiration())
                .user(modelMapper.map(userDetails.getUser(), UserResponse.class))
                .build();
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
}
