package com.bookstore.app.service.impl;

import com.bookstore.app.entity.User;
import com.bookstore.app.entity.VerificationToken;
import com.bookstore.app.repository.VerificationTokenRepository;
import com.bookstore.app.service.VerificationTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VerificationTokenServiceImpl implements VerificationTokenService {

    VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken createToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(1));
        System.out.println(user);
        return verificationTokenRepository.save(verificationToken);
    }
}
