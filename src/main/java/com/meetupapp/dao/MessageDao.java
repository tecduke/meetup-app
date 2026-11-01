package com.meetupapp.dao;

import com.meetupapp.model.Message;
import com.meetupapp.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    public void save(Message message) throws SQLException {
        String sql = "INSERT INTO messages (meetup_id, sender_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, message.getMeetupId());
            ps.setLong(2, message.getSenderId());
            ps.setString(3, message.getContent());
            ps.executeUpdate();
        }
    }

    public List<Message> findByMeetupId(Long meetupId) throws SQLException {
        String sql = "SELECT m.*, u.name AS sender_name FROM messages m JOIN users u ON u.id = m.sender_id WHERE m.meetup_id = ? ORDER BY m.created_at ASC";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, meetupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setMeetupId(rs.getLong("meetup_id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setContent(rs.getString("content"));
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    if (createdAt != null) {
                        message.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    message.setSenderName(rs.getString("sender_name"));
                    messages.add(message);
                }
            }
        }
        return messages;
    }
}
