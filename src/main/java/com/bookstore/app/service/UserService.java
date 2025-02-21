package com.bookstore.app.service;

import com.bookstore.app.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
}
