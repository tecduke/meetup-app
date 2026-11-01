package com.meetupapp.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private Long meetupId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;
    private String senderName;

    public Message() {}

    public Message(Long id, Long meetupId, Long senderId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.meetupId = meetupId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMeetupId() { return meetupId; }
    public void setMeetupId(Long meetupId) { this.meetupId = meetupId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
}
