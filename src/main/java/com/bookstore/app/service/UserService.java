package com.bookstore.app.service;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RegisterRequest;
import com.bookstore.app.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse createUserWithRole(RegisterRequest registerRequest, RoleType roleType);
    UserResponse updateUser(Long id, RegisterRequest registerRequest);
    void deleteUser(Long id);
}
