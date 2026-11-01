package com.meetupapp.servlet;

import com.meetupapp.dao.MeetupDao;
import com.meetupapp.dao.MessageDao;
import com.meetupapp.dao.NotificationDao;
import com.meetupapp.dao.ParticipantDao;
import com.meetupapp.model.Meetup;
import com.meetupapp.model.Message;
import com.meetupapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ChatServlet", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {
    private final MeetupDao meetupDao = new MeetupDao();
    private final MessageDao messageDao = new MessageDao();
    private final ParticipantDao participantDao = new ParticipantDao();
    private final NotificationDao notificationDao = new NotificationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        Long meetupId = parseLong(request.getParameter("meetupId"));
        if (meetupId == null) {
            response.sendRedirect(request.getContextPath() + "/meetups");
            return;
        }

        try {
            Meetup meetup = meetupDao.findById(meetupId);
            if (meetup == null || !participantDao.isJoined(meetupId, user.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to access this chat.");
                return;
            }

            if ("json".equalsIgnoreCase(request.getParameter("format"))) {
                List<Message> messages = messageDao.findByMeetupId(meetupId);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print("{\"messages\":[");
                for (int i = 0; i < messages.size(); i++) {
                    Message message = messages.get(i);
                    out.print("{\"senderName\":\"" + escapeJson(message.getSenderName()) + "\",\"content\":\"" + escapeJson(message.getContent()) + "\",\"createdAt\":\"" + message.getCreatedAt() + "\"}");
                    if (i < messages.size() - 1) {
                        out.print(",");
                    }
                }
                out.print("]}");
                return;
            }

            request.setAttribute("meetup", meetup);
            request.setAttribute("messages", messageDao.findByMeetupId(meetupId));
            request.setAttribute("participants", participantDao.findParticipants(meetupId));
            request.getRequestDispatcher("/WEB-INF/views/chat.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to load chat", e);
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
        Long meetupId = parseLong(request.getParameter("meetupId"));
        String content = request.getParameter("content");

        if (meetupId == null || content == null || content.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/chat?meetupId=" + meetupId);
            return;
        }

        try {
            Meetup meetup = meetupDao.findById(meetupId);
            if (meetup == null || !participantDao.isJoined(meetupId, user.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not allowed to send messages.");
                return;
            }

            Message message = new Message();
            message.setMeetupId(meetupId);
            message.setSenderId(user.getId());
            message.setContent(content.trim());
            messageDao.save(message);

            List<com.meetupapp.model.Participant> participants = participantDao.findParticipants(meetupId);
            for (com.meetupapp.model.Participant participant : participants) {
                if (!participant.getUserId().equals(user.getId())) {
                    notificationDao.create(participant.getUserId(), user.getName() + " sent a new message in " + meetup.getTitle(), "chat_message", meetupId);
                }
            }

            response.sendRedirect(request.getContextPath() + "/chat?meetupId=" + meetupId);
        } catch (SQLException e) {
            throw new ServletException("Unable to send message", e);
        }
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
