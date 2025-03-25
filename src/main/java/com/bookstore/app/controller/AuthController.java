package com.bookstore.app.controller;

import com.bookstore.app.dto.request.LoginRequest;
import com.bookstore.app.dto.request.ResetPasswordRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.JwtTokenResponse;
import com.bookstore.app.dto.response.UserProfileResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.entity.User;
import com.bookstore.app.event.OnResetPasswordEvent;
import com.bookstore.app.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {

    AuthService authService;
    ModelMapper modelMapper;
    ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
            ) {
        log.info("Login user with email: {}", loginRequest.getEmail());
        return ResponseEntity.ok(
                ApiResponse.<JwtTokenResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(authService.login(loginRequest, response))
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        log.info("Logout user");
        authService.logout(response);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logout successful")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam("email") String email,
                                                           HttpServletRequest request) {
        log.info("Reset password for user with email: {}", email);
        User user = modelMapper.map(authService.resetPassword(email), User.class);
         String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
                 + "/api/v1/auth/confirm-reset-password";
         applicationEventPublisher.publishEvent(new OnResetPasswordEvent(user, appUrl));
         return ResponseEntity.ok(
                 ApiResponse.<Void>builder()
                         .success(true)
                         .message("Password reset link sent to your e-mail address. Please check your inbox.")
                         .data(null)
                         .build()
         );
    }

    @GetMapping("confirm-reset-password")
    public ResponseEntity<ApiResponse<Long>> confirmResetPassword(@RequestParam("token") String token) {
        log.info("Confirm reset password with token: {}", token);
        return ResponseEntity.ok(
                ApiResponse.<Long>builder()
                        .success(true)
                        .message("Confirm reset password successful")
                        .data(authService.confirmResetPassword(token))
                        .build()
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest
            ) {
        log.info("Change password for user with id: {}", resetPasswordRequest.getUserId());
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Change password of user with id: " + resetPasswordRequest.getUserId() + " successfully!")
                        .data(authService.changPassword(resetPasswordRequest))
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe() {
        log.info("Get me");
        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .success(true)
                        .message("Get me successfully!")
                        .data(authService.getMe())
                        .build()
        );
    }

    @GetMapping("refresh-token")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> refreshToken(HttpServletRequest request,
                                                                      HttpServletResponse response) {
        log.info("Refresh token");
        return ResponseEntity.ok(
                ApiResponse.<JwtTokenResponse>builder()
                        .success(true)
                        .message("Refresh token successfully!")
                        .data(authService.refreshToken(request, response))
                        .build()
        );
    }
}
