package com.meetupapp.servlet;

import com.meetupapp.dao.NotificationDao;
import com.meetupapp.model.Notification;
import com.meetupapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "NotificationServlet", urlPatterns = "/notifications")
public class NotificationServlet extends HttpServlet {
    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        try {
            List<Notification> notifications = notificationDao.findByUser(user.getId());
            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/WEB-INF/views/notifications.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load notifications", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        try {
            if ("mark-read".equals(action)) {
                Long notificationId = parseLong(request.getParameter("id"));
                if (notificationId != null) {
                    notificationDao.markAsRead(notificationId, user.getId());
                }
            } else if ("mark-all-read".equals(action)) {
                notificationDao.markAllAsRead(user.getId());
            }
            response.sendRedirect(request.getContextPath() + "/notifications");
        } catch (SQLException e) {
            throw new ServletException("Unable to update notifications", e);
        }
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
