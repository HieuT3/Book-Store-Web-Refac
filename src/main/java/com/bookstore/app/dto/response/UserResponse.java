package com.bookstore.app.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long userId;
    String email;
    String fullName;
    String phone;
    String address;
    boolean isActive;
    Set<RoleResponse> roles;
}
