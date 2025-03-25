package com.bookstore.app.service;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RegisterRequest;
import com.bookstore.app.dto.response.UserResponse;

public interface RegistrationService {
    UserResponse register(RegisterRequest registerRequest, RoleType roleType);
    UserResponse confirmRegistration(String token);
    UserResponse resendEmail(String email);
}
