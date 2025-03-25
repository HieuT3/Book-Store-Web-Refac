package com.bookstore.app.event;

import com.bookstore.app.entity.User;
import com.bookstore.app.service.MailService;
import com.bookstore.app.service.VerificationTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationCompleteListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    MailService mailService;
    VerificationTokenService verificationTokenService;

    @Override

    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.createToken(user, token);

        String userEmail = user.getEmail();
        String subject = "Registration Confirmation";
        String text = "To confirm your e-mail address, please click the link below:\n"
                + event.getAppUrl() + "?token=" + token;
        mailService.sendEmail(userEmail, subject, text);
        log.info("Registration confirmation e-mail sent to {}", userEmail);
    }
}
