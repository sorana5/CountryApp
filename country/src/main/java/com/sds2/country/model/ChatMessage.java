package com.sds2.country.model;

import java.time.Instant;

public class ChatMessage {
    private String sender;
    private String content;
    private Instant timestamp = Instant.now();

    public ChatMessage() {}
    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}