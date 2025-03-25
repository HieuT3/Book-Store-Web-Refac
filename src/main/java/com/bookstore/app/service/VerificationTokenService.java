package com.bookstore.app.service;

import com.bookstore.app.entity.User;
import com.bookstore.app.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createToken(User user, String token);
}
