package com.meetupapp.dao;

import com.meetupapp.model.Meetup;
import com.meetupapp.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetupDao {
    public void save(Meetup meetup) throws SQLException {
        String sql = "INSERT INTO meetups (organizer_id, title, description, category, meetup_type, location, event_datetime, image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetup.getOrganizerId());
            ps.setString(2, meetup.getTitle());
            ps.setString(3, meetup.getDescription());
            ps.setString(4, meetup.getCategory());
            ps.setString(5, meetup.getMeetupType());
            ps.setString(6, meetup.getLocation());
            ps.setTimestamp(7, Timestamp.valueOf(meetup.getEventDateTime()));
            ps.setString(8, meetup.getImagePath());
            ps.executeUpdate();
        }
    }

    public Meetup findById(Long meetupId) throws SQLException {
        String sql = "SELECT m.*, u.name AS organizer_name, (SELECT COUNT(*) FROM participants p WHERE p.meetup_id = m.id) AS participant_count " +
                "FROM meetups m JOIN users u ON u.id = m.organizer_id WHERE m.id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Meetup meetup = mapRow(rs);
                    meetup.setParticipantCount(rs.getInt("participant_count"));
                    meetup.setOrganizerName(rs.getString("organizer_name"));
                    return meetup;
                }
            }
        }
        return null;
    }

    public List<Meetup> findAll() throws SQLException {
        String sql = "SELECT m.*, u.name AS organizer_name, (SELECT COUNT(*) FROM participants p WHERE p.meetup_id = m.id) AS participant_count " +
                "FROM meetups m JOIN users u ON u.id = m.organizer_id ORDER BY m.event_datetime ASC";
        List<Meetup> meetups = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Meetup meetup = mapRow(rs);
                meetup.setParticipantCount(rs.getInt("participant_count"));
                meetup.setOrganizerName(rs.getString("organizer_name"));
                meetups.add(meetup);
            }
        }
        return meetups;
    }

    public List<Meetup> findByOrganizerId(Long organizerId) throws SQLException {
        String sql = "SELECT m.*, u.name AS organizer_name, (SELECT COUNT(*) FROM participants p WHERE p.meetup_id = m.id) AS participant_count " +
                "FROM meetups m JOIN users u ON u.id = m.organizer_id WHERE m.organizer_id = ? ORDER BY m.event_datetime ASC";
        List<Meetup> meetups = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, organizerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meetup meetup = mapRow(rs);
                    meetup.setOrganizerName(rs.getString("organizer_name"));
                    meetup.setParticipantCount(rs.getInt("participant_count"));
                    meetups.add(meetup);
                }
            }
        }
        return meetups;
    }

    public List<Meetup> findMeetupsJoinedByUser(Long userId) throws SQLException {
        String sql = "SELECT m.*, u.name AS organizer_name, (SELECT COUNT(*) FROM participants p WHERE p.meetup_id = m.id) AS participant_count FROM meetups m " +
                "JOIN users u ON u.id = m.organizer_id JOIN participants p ON p.meetup_id = m.id WHERE p.user_id = ? ORDER BY m.event_datetime ASC";
        List<Meetup> meetups = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meetup meetup = mapRow(rs);
                    meetup.setOrganizerName(rs.getString("organizer_name"));
                    meetup.setParticipantCount(rs.getInt("participant_count"));
                    meetups.add(meetup);
                }
            }
        }
        return meetups;
    }

    public void update(Meetup meetup) throws SQLException {
        String sql = "UPDATE meetups SET title = ?, description = ?, category = ?, meetup_type = ?, location = ?, event_datetime = ?, image_path = ? WHERE id = ? AND organizer_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, meetup.getTitle());
            ps.setString(2, meetup.getDescription());
            ps.setString(3, meetup.getCategory());
            ps.setString(4, meetup.getMeetupType());
            ps.setString(5, meetup.getLocation());
            ps.setTimestamp(6, Timestamp.valueOf(meetup.getEventDateTime()));
            ps.setString(7, meetup.getImagePath());
            ps.setLong(8, meetup.getId());
            ps.setLong(9, meetup.getOrganizerId());
            ps.executeUpdate();
        }
    }

    public void delete(Long meetupId, Long organizerId) throws SQLException {
        String sql = "DELETE FROM meetups WHERE id = ? AND organizer_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            ps.setLong(2, organizerId);
            ps.executeUpdate();
        }
    }

    private Meetup mapRow(ResultSet rs) throws SQLException {
        Meetup meetup = new Meetup();
        meetup.setId(rs.getLong("id"));
        meetup.setOrganizerId(rs.getLong("organizer_id"));
        meetup.setTitle(rs.getString("title"));
        meetup.setDescription(rs.getString("description"));
        meetup.setCategory(rs.getString("category"));
        meetup.setMeetupType(rs.getString("meetup_type"));
        meetup.setLocation(rs.getString("location"));
        Timestamp eventTime = rs.getTimestamp("event_datetime");
        if (eventTime != null) {
            meetup.setEventDateTime(eventTime.toLocalDateTime());
        }
        meetup.setImagePath(rs.getString("image_path"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            meetup.setCreatedAt(createdAt.toLocalDateTime());
        }
        return meetup;
    }
}
