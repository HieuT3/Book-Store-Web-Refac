package com.bookstore.app.service;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.request.ResetPasswordRequest;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.dto.response.UserProfileResponse;
import com.bookstore.app.dto.response.UserResponse;

public interface AuthService {
    JwtTokenResponse login(LoginRequest loginRequest);
    UserResponse resetPassword(String email);
    Long confirmResetPassword(String token);
    UserResponse changPassword(ResetPasswordRequest resetPasswordRequest);
    UserProfileResponse getMe();
}
