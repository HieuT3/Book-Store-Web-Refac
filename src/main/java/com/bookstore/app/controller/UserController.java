package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        .message("Get all user")
                        .data(userService.getAll())
                        .build()
        );
    }
}
