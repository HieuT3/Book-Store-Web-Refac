package com.bookstore.app.service;

public interface MailService {
    void sendEmail(String to, String subject, String text);
}
