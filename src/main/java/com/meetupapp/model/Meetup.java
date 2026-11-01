package com.meetupapp.model;

import java.time.LocalDateTime;

public class Meetup {
    private Long id;
    private Long organizerId;
    private String title;
    private String description;
    private String category;
    private String meetupType;
    private String location;
    private LocalDateTime eventDateTime;
    private String imagePath;
    private LocalDateTime createdAt;
    private String organizerName;
    private int participantCount;

    public Meetup() {}

    public Meetup(Long id, Long organizerId, String title, String description, String category,
                 String meetupType, String location, LocalDateTime eventDateTime, String imagePath,
                 LocalDateTime createdAt) {
        this.id = id;
        this.organizerId = organizerId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.meetupType = meetupType;
        this.location = location;
        this.eventDateTime = eventDateTime;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMeetupType() { return meetupType; }
    public void setMeetupType(String meetupType) { this.meetupType = meetupType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getEventDateTime() { return eventDateTime; }
    public void setEventDateTime(LocalDateTime eventDateTime) { this.eventDateTime = eventDateTime; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getOrganizerName() { return organizerName; }
    public void setOrganizerName(String organizerName) { this.organizerName = organizerName; }

    public int getParticipantCount() { return participantCount; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }
}
