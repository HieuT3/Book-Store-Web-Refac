package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.exception.BadCredentialsException;
import com.bookstore.app.security.CustomerUserDetails;
import com.bookstore.app.security.JwtAuthenticationProvider;
import com.bookstore.app.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserDetailsService userDetailsService;
    JwtAuthenticationProvider jwtAuthenticationProvider;
    PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public JwtTokenResponse login(LoginRequest loginRequest) {
        CustomerUserDetails userDetails = (CustomerUserDetails) userDetailsService.loadUserByUsername(loginRequest.getEmail());
        if(!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword()))
            throw new BadCredentialsException("Invalid email or password");
        return JwtTokenResponse.builder()
                .accessToken(jwtAuthenticationProvider.generateAccessToken(userDetails))
                .accessTokenExpiration(jwtAuthenticationProvider.getJwtAccessTokenExpiration())
                .refreshToken(jwtAuthenticationProvider.generateRefreshToken(userDetails))
                .refreshTokenExpiration(jwtAuthenticationProvider.getJwtRefreshTokenExpiration())
                .user(modelMapper.map(userDetails.getUser(), UserResponse.class))
                .build();
    }
}
