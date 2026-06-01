package com.sds2.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendCountryNotification(String email, String type, String countryName) {
        String subject = "Country " + type.toLowerCase() + ": " + countryName;
        String body = "Hello,\n\nThe country '" + countryName +
                "' was successfully " + type.toLowerCase() + ".";
        send(email, subject, body);
    }

    public void sendFunFactDeletedNotification(String email,
                                               String content,
                                               String countryName) {
        String subject = "Your fun fact was removed";
        String body = "Hello,\n\nYour fun fact about " + countryName +
                " was removed by an administrator.\n\n" +
                "Removed fact: \"" + content + "\"";
        send(email, subject, body);
    }

    private void send(String to, String subject, String body) {
        if (mailSender == null) {
            System.out.println("Mail not configured. Would send to: "
                    + to + " | " + subject);
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }
}