package com.bookstore.app.service;

import com.bookstore.app.entity.PasswordResetToken;
import com.bookstore.app.entity.User;

public interface PasswordResetTokenService {
    PasswordResetToken createToken(User user, String token);
}
