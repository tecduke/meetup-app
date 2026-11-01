package com.meetupapp.dao;

import com.meetupapp.model.Participant;
import com.meetupapp.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDao {
    public boolean isJoined(Long meetupId, Long userId) throws SQLException {
        String sql = "SELECT 1 FROM participants WHERE meetup_id = ? AND user_id = ? LIMIT 1";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void join(Long meetupId, Long userId) throws SQLException {
        String sql = "INSERT INTO participants (meetup_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void leave(Long meetupId, Long userId) throws SQLException {
        String sql = "DELETE FROM participants WHERE meetup_id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public List<Participant> findParticipants(Long meetupId) throws SQLException {
        String sql = "SELECT p.*, u.name AS user_name FROM participants p JOIN users u ON p.user_id = u.id WHERE p.meetup_id = ? ORDER BY p.joined_at ASC";
        List<Participant> participants = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Participant participant = new Participant();
                    participant.setId(rs.getLong("id"));
                    participant.setMeetupId(rs.getLong("meetup_id"));
                    participant.setUserId(rs.getLong("user_id"));
                    participant.setJoinedAt(rs.getTimestamp("joined_at").toLocalDateTime());
                    participant.setUserName(rs.getString("user_name"));
                    participants.add(participant);
                }
            }
        }
        return participants;
    }
}
