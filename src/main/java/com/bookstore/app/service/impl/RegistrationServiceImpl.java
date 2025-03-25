package com.bookstore.app.service.impl;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RegisterRequest;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.entity.User;
import com.bookstore.app.entity.VerificationToken;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.exception.TokenExpiredException;
import com.bookstore.app.repository.UserRepository;
import com.bookstore.app.repository.VerificationTokenRepository;
import com.bookstore.app.service.RegistrationService;
import com.bookstore.app.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationServiceImpl implements RegistrationService {

    UserRepository userRepository;
    UserService userService;
    VerificationTokenRepository verificationTokenRepository;
    ModelMapper modelMapper;

    @Override
    public UserResponse register(RegisterRequest registerRequest, RoleType roleType) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
            throw new ResourceAlreadyExistsException("User with email " + registerRequest.getEmail() + " already exists");
        return userService.createUserWithRole(registerRequest, roleType);
    }

    @Override
    public UserResponse confirmRegistration(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Verification token not found"));
        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new TokenExpiredException("Verification token has expired");
        User user = verificationToken.getUser();
        if(user.isActive())
            throw new IllegalStateException("User with email " + user.getEmail() + " is already active");
        user.setActive(true);
        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public UserResponse resendEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        if(user.isActive())
            throw new IllegalStateException("User with email " + email + " is already active");
        verificationTokenRepository.findByUser(user).ifPresent(verificationTokenRepository::delete);
        return modelMapper.map(user, UserResponse.class);
    }
}
