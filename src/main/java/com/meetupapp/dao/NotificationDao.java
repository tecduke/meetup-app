package com.meetupapp.dao;

import com.meetupapp.model.Notification;
import com.meetupapp.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {
    public void create(Long userId, String message, String relatedType, Long relatedId) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message, related_type, related_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setString(2, message);
            ps.setString(3, relatedType);
            if (relatedId != null) {
                ps.setLong(4, relatedId);
            } else {
                ps.setNull(4, java.sql.Types.BIGINT);
            }
            ps.executeUpdate();
        }
    }

    public List<Notification> findByUser(Long userId) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification();
                    notification.setId(rs.getLong("id"));
                    notification.setUserId(rs.getLong("user_id"));
                    notification.setMessage(rs.getString("message"));
                    notification.setRelatedType(rs.getString("related_type"));
                    if (rs.getObject("related_id") != null) {
                        notification.setRelatedId(rs.getLong("related_id"));
                    }
                    notification.setRead(rs.getBoolean("is_read"));
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    if (createdAt != null) {
                        notification.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    notifications.add(notification);
                }
            }
        }
        return notifications;
    }

    public int countUnread(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void markAsRead(Long notificationId, Long userId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = 1 WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, notificationId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void markAllAsRead(Long userId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = 1 WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
