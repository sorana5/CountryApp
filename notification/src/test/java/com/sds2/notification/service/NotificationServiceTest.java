package com.sds2.notification.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void sendCountryNotification_sendsEmailWithCorrectSubject() {
        notificationService.sendCountryNotification(
                "admin@test.com", "CREATED", "France");

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertEquals("admin@test.com", sent.getTo()[0]);
        assertTrue(sent.getSubject().contains("France"));
        assertTrue(sent.getSubject().contains("created"));
    }

    @Test
    void sendFunFactDeletedNotification_sendsEmailWithContent() {
        notificationService.sendFunFactDeletedNotification(
                "user@test.com", "Fun fact content", "Japan");

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertEquals("user@test.com", sent.getTo()[0]);
        assertTrue(sent.getText().contains("Fun fact content"));
        assertTrue(sent.getText().contains("Japan"));
    }

    @Test
    void sendEmail_doesNotThrow_whenMailSenderIsNull() {
        NotificationService serviceWithoutMail = new NotificationService(null);

        assertDoesNotThrow(() ->
                serviceWithoutMail.sendCountryNotification(
                        "test@test.com", "CREATED", "France"));
    }
}