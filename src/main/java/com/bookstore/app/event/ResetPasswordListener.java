package com.bookstore.app.event;

import com.bookstore.app.entity.User;
import com.bookstore.app.service.MailService;
import com.bookstore.app.service.PasswordResetTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    PasswordResetTokenService passwordResetTokenService;
    MailService mailService;

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        createResetPasswordToken(event);
    }

    private void createResetPasswordToken(OnResetPasswordEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createToken(user,token);
        String to = user.getEmail();
        String subject = "Password Reset Request";
        String text = "To reset your password, please click the link below:\n"
                + event.getAppUrl() + "?token=" + token;
        mailService.sendEmail(to, subject, text);
    }
}
