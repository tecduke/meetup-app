package com.meetupapp.servlet;

import com.meetupapp.dao.MeetupDao;
import com.meetupapp.dao.NotificationDao;
import com.meetupapp.dao.ParticipantDao;
import com.meetupapp.model.Meetup;
import com.meetupapp.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
@WebServlet(name = "MeetupServlet", urlPatterns = "/meetups")
public class MeetupServlet extends HttpServlet {
    private final MeetupDao meetupDao = new MeetupDao();
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
        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/WEB-INF/views/create-meetup.jsp").forward(request, response);
                return;
            }

            if ("detail".equals(action)) {
                Long meetupId = parseLong(request.getParameter("id"));
                if (meetupId == null) {
                    response.sendRedirect(request.getContextPath() + "/meetups");
                    return;
                }
                Meetup meetup = meetupDao.findById(meetupId);
                if (meetup == null) {
                    response.sendRedirect(request.getContextPath() + "/meetups");
                    return;
                }

                boolean joined = participantDao.isJoined(meetupId, user.getId());
                request.setAttribute("meetup", meetup);
                request.setAttribute("joined", joined);
                request.setAttribute("participants", participantDao.findParticipants(meetupId));
                request.getRequestDispatcher("/WEB-INF/views/meetup-detail.jsp").forward(request, response);
                return;
            }

            List<Meetup> meetups = meetupDao.findAll();
            request.setAttribute("user", user);
            request.setAttribute("meetups", meetups);
            request.getRequestDispatcher("/WEB-INF/views/join-meetup.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error loading meetups", e);
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
            if ("create".equals(action)) {
                handleCreate(request, response, user);
                return;
            }
            if ("join".equals(action)) {
                handleJoin(request, response, user);
                return;
            }
            if ("leave".equals(action)) {
                handleLeave(request, response, user);
                return;
            }
            if ("update".equals(action)) {
                handleUpdate(request, response, user);
                return;
            }
            if ("delete".equals(action)) {
                handleDelete(request, response, user);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/meetups");
        } catch (SQLException e) {
            throw new ServletException("Meetup processing failed", e);
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, SQLException, ServletException {
        String title = trim(request.getParameter("title"));
        String description = trim(request.getParameter("description"));
        String category = trim(request.getParameter("category"));
        String meetupType = trim(request.getParameter("meetupType"));
        String location = trim(request.getParameter("location"));
        String eventDateTimeRaw = trim(request.getParameter("eventDateTime"));

        if (isBlank(title) || isBlank(description) || isBlank(category) || isBlank(meetupType) || isBlank(location) || isBlank(eventDateTimeRaw)) {
            request.setAttribute("error", "All meetup fields are required.");
            request.getRequestDispatcher("/WEB-INF/views/create-meetup.jsp").forward(request, response);
            return;
        }

        LocalDateTime eventDateTime;
        try {
            eventDateTime = LocalDateTime.parse(eventDateTimeRaw.replace("T", "T"));
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Please enter a valid date and time.");
            request.getRequestDispatcher("/WEB-INF/views/create-meetup.jsp").forward(request, response);
            return;
        }

        String imagePath = saveUploadedImage(request);

        Meetup meetup = new Meetup();
        meetup.setOrganizerId(user.getId());
        meetup.setTitle(title);
        meetup.setDescription(description);
        meetup.setCategory(category);
        meetup.setMeetupType(meetupType);
        meetup.setLocation(location);
        meetup.setEventDateTime(eventDateTime);
        meetup.setImagePath(imagePath);
        meetupDao.save(meetup);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void handleJoin(HttpServletRequest request, HttpServletResponse response, User user) throws SQLException, IOException {
        Long meetupId = parseLong(request.getParameter("id"));
        if (meetupId == null) {
            response.sendRedirect(request.getContextPath() + "/meetups");
            return;
        }

        Meetup meetup = meetupDao.findById(meetupId);
        if (meetup == null || participantDao.isJoined(meetupId, user.getId())) {
            response.sendRedirect(request.getContextPath() + "/meetups");
            return;
        }

        participantDao.join(meetupId, user.getId());
        notificationDao.create(meetup.getOrganizerId(), user.getName() + " joined your meetup: " + meetup.getTitle(), "meetup_join", meetupId);
        response.sendRedirect(request.getContextPath() + "/meetups?action=detail&id=" + meetupId);
    }

    private void handleLeave(HttpServletRequest request, HttpServletResponse response, User user) throws SQLException, IOException {
        Long meetupId = parseLong(request.getParameter("id"));
        if (meetupId != null) {
            participantDao.leave(meetupId, user.getId());
        }
        response.sendRedirect(request.getContextPath() + "/meetups");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, User user) throws SQLException, IOException, ServletException {
        Long meetupId = parseLong(request.getParameter("id"));
        if (meetupId == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        Meetup existing = meetupDao.findById(meetupId);
        if (existing == null || !existing.getOrganizerId().equals(user.getId())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String title = trim(request.getParameter("title"));
        String description = trim(request.getParameter("description"));
        String category = trim(request.getParameter("category"));
        String meetupType = trim(request.getParameter("meetupType"));
        String location = trim(request.getParameter("location"));
        String eventDateTimeRaw = trim(request.getParameter("eventDateTime"));

        if (isBlank(title) || isBlank(description) || isBlank(category) || isBlank(meetupType) || isBlank(location) || isBlank(eventDateTimeRaw)) {
            request.setAttribute("error", "Please fill all meetup fields.");
            request.setAttribute("meetup", existing);
            request.getRequestDispatcher("/WEB-INF/views/create-meetup.jsp").forward(request, response);
            return;
        }

        LocalDateTime eventDateTime = LocalDateTime.parse(eventDateTimeRaw);
        String imagePath = saveUploadedImage(request);
        if (imagePath == null) {
            imagePath = existing.getImagePath();
        }

        existing.setTitle(title);
        existing.setDescription(description);
        existing.setCategory(category);
        existing.setMeetupType(meetupType);
        existing.setLocation(location);
        existing.setEventDateTime(eventDateTime);
        existing.setImagePath(imagePath);

        meetupDao.update(existing);
        response.sendRedirect(request.getContextPath() + "/meetups?action=detail&id=" + meetupId);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, User user) throws SQLException, IOException {
        Long meetupId = parseLong(request.getParameter("id"));
        if (meetupId != null) {
            meetupDao.delete(meetupId, user.getId());
        }
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private String saveUploadedImage(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("image");
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase() : "jpg";
        String[] allowed = {"jpg", "jpeg", "png", "gif", "webp"};
        boolean valid = false;
        for (String ext : allowed) {
            if (ext.equals(extension)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return null;
        }

        String uploadDir = request.getServletContext().getRealPath("/uploads");
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String storageName = System.currentTimeMillis() + "_" + fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String targetPath = uploadDir + File.separator + storageName;
        filePart.write(targetPath);
        return "uploads/" + storageName;
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Long parseLong(String value) {
        try {
            return value == null || value.isBlank() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
