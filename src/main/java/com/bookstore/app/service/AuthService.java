package com.bookstore.app.service;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.request.ResetPasswordRequest;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.dto.response.UserProfileResponse;
import com.bookstore.app.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtTokenResponse login(LoginRequest loginRequest, HttpServletResponse response);
    void logout(HttpServletResponse response);
    UserResponse resetPassword(String email);
    Long confirmResetPassword(String token);
    UserResponse changPassword(ResetPasswordRequest resetPasswordRequest);
    UserProfileResponse getMe();
    JwtTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
