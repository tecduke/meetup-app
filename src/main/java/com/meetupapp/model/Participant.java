package com.meetupapp.model;

import java.time.LocalDateTime;

public class Participant {
    private Long id;
    private Long meetupId;
    private Long userId;
    private LocalDateTime joinedAt;
    private String userName;

    public Participant() {}

    public Participant(Long id, Long meetupId, Long userId, LocalDateTime joinedAt) {
        this.id = id;
        this.meetupId = meetupId;
        this.userId = userId;
        this.joinedAt = joinedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMeetupId() { return meetupId; }
    public void setMeetupId(Long meetupId) { this.meetupId = meetupId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
