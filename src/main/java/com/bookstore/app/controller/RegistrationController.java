package com.bookstore.app.controller;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RegisterRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.entity.User;
import com.bookstore.app.event.OnRegistrationCompleteEvent;
import com.bookstore.app.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationController {

    RegistrationService registrationService;
    ApplicationEventPublisher applicationEventPublisher;
    ModelMapper modelMapper;

    @PostMapping("")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest registerRequest, HttpServletRequest request
            ) {
        log.info("Register user with email: {}", registerRequest.getEmail());
        UserResponse userResponse = registrationService.register(registerRequest, RoleType.USER);
        User user = modelMapper.map(userResponse, User.class);
        String appUrl = buildUrl(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User registered successfully! Please check your email for confirmation")
                        .data(userResponse)
                        .build()
        );
    }

    @GetMapping("/confirm-registration")
    public ResponseEntity<ApiResponse<Void>> confirmRegistration(
            @RequestParam("token") String token
    ) {
        registrationService.confirmRegistration(token);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("User confirmed successfully! Please login to continue")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("resend")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(@RequestParam("email") String email,
                                                         HttpServletRequest request) {
        log.info("Resend email for user with email: {}", email);
        User user = modelMapper.map(registrationService.resendEmail(email), User.class);
        String appUrl = buildUrl(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Email sent successfully")
                        .data(null)
                        .build()
        );
    }

    private String buildUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/"
                + "api/v1/register/confirm-registration";
    }
}
