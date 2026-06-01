package com.sds2.notification.controller;

import com.sds2.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/notify")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/country")
    public ResponseEntity<Void> notifyCountry(@RequestBody Map<String, String> body) {
        notificationService.sendCountryNotification(
                body.get("email"),
                body.get("type"),
                body.get("countryName"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/funfact-deleted")
    public ResponseEntity<Void> notifyFunFactDeleted(@RequestBody Map<String, String> body) {
        notificationService.sendFunFactDeletedNotification(
                body.get("email"),
                body.get("content"),
                body.get("countryName"));
        return ResponseEntity.ok().build();
    }
}