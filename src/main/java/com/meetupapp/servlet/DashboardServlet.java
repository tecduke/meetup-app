package com.meetupapp.servlet;

import com.meetupapp.dao.MeetupDao;
import com.meetupapp.dao.NotificationDao;
import com.meetupapp.model.Meetup;
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

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private final MeetupDao meetupDao = new MeetupDao();
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
            List<Meetup> joinedMeetups = meetupDao.findMeetupsJoinedByUser(user.getId());
            List<Meetup> myMeetups = meetupDao.findByOrganizerId(user.getId());
            int unreadNotifications = notificationDao.countUnread(user.getId());

            request.setAttribute("user", user);
            request.setAttribute("joinedMeetups", joinedMeetups);
            request.setAttribute("myMeetups", myMeetups);
            request.setAttribute("unreadNotifications", unreadNotifications);
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load dashboard", e);
        }
    }
}
