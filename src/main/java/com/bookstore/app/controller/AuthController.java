package com.bookstore.app.controller;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        log.info("Login request");
        return ResponseEntity.ok(
                ApiResponse.<JwtTokenResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(authService.login(loginRequest))
                        .build()
        );
    }
}
