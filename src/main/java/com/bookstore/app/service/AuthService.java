package com.bookstore.app.service;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.response.JwtTokenResponse;

public interface AuthService {
    JwtTokenResponse login(LoginRequest loginRequest);
}
