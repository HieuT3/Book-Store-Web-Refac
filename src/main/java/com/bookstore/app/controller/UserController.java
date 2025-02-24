package com.bookstore.app.controller;

import com.bookstore.app.dto.request.UserRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        log.info("Get all user");
        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .message("Get all user successfully!")
                        .data(userService.getAll())
                        .build()
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        log.info("Get user by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Get user by id: " + id + " successfully!")
                        .data(userService.getUserById(id))
                        .build()
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@PathVariable String email) {
        log.info("Get user by email: {}", email);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Get user by email: " + email + " successfully!")
                        .data(userService.getUserByEmail(email))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest userRequest
            ) {
        log.info("Create user with email: {}", userRequest.getEmail());
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Create user with email: " + userRequest.getEmail() + " successfully!")
                        .data(userService.createUserWithUserRole(userRequest))
                        .build()
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest
            ) {
        log.info("Update user with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Update user with id: " + id + " successfully!")
                        .data(userService.updateUser(id, userRequest))
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        log.info("Delete user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete user with id: " + id + " successfully!")
                        .data(null)
                        .build()
        );
    }
}
