package com.bookstore.app.service.impl;

import com.bookstore.app.entity.PasswordResetToken;
import com.bookstore.app.entity.User;
import com.bookstore.app.repository.PasswordResetTokenRepository;
import com.bookstore.app.service.PasswordResetTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken createToken(User user, String token) {
        return passwordResetTokenRepository.save(
                PasswordResetToken.builder()
                        .user(user)
                        .token(token)
                        .expiryDate(LocalDateTime.now().plusHours(1))
                        .build()
        );
    }
}
