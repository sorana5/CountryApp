package com.sds2.country.controller;

import com.sds2.country.model.ChatMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    @MessageMapping("/chat.send")
//    public void sendMessage(@Payload ChatMessage message, Principal principal) {
//        message.setSender(principal != null ?
//                principal.getName() : "Anonymous");
//        messagingTemplate.convertAndSend("/topic/chat", message);
//    }
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage message) {
    // use whatever sender the client sent
    // if empty or null, default to Anonymous
    if (message.getSender() == null || message.getSender().isBlank()) {
        message.setSender("Anonymous");
    }
    message.setTimestamp(java.time.Instant.now());
    messagingTemplate.convertAndSend("/topic/chat", message);
    }
}