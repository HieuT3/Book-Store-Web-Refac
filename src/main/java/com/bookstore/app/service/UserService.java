package com.bookstore.app.service;

import com.bookstore.app.dto.request.UserRequest;
import com.bookstore.app.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse createUserWithUserRole(UserRequest userRequest);
    UserResponse createUserWithAdminRole(UserRequest userRequest);
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
}
