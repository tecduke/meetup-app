<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">Meetup</a>
        <div class="ms-auto d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/notifications" class="btn btn-light btn-sm position-relative">
                Notifications
                <c:if test="${unreadNotifications > 0}">
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">${unreadNotifications}</span>
                </c:if>
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <div class="row g-4 align-items-start">
        <div class="col-lg-4">
            <div class="card border-0 shadow-sm">
                <div class="card-body text-center">
                    <div class="avatar-box mx-auto mb-3">
                        <c:if test="${not empty user.profileImage}">
                            <img src="${pageContext.request.contextPath}/${user.profileImage}" class="img-fluid rounded-circle" alt="Profile image">
                        </c:if>
                        <c:if test="${empty user.profileImage}">
                            <span>${user.name.substring(0,1)}</span>
                        </c:if>
                    </div>
                    <h4>${user.name}</h4>
                    <p class="text-muted mb-1">${user.email}</p>
                    <p class="text-muted mb-0">${user.city}</p>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="row g-3">
                <div class="col-md-6">
                    <a href="${pageContext.request.contextPath}/meetups" class="action-card d-block">
                        <div class="card h-100 border-0 shadow-sm">
                            <div class="card-body text-center py-5">
                                <h3>Join Meetup</h3>
                                <p>Discover active plans in your city</p>
                            </div>
                        </div>
                    </a>
                </div>
                <div class="col-md-6">
                    <a href="${pageContext.request.contextPath}/meetups?action=create" class="action-card d-block">
                        <div class="card h-100 border-0 shadow-sm">
                            <div class="card-body text-center py-5">
                                <h3>Create Meetup</h3>
                                <p>Start a new event with friends</p>
                            </div>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="row mt-4 g-4">
        <div class="col-lg-6">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h4 class="mb-3">Your meetups</h4>
                    <c:if test="${empty joinedMeetups}">
                        <p class="text-muted mb-0">You have not joined any meetup yet.</p>
                    </c:if>
                    <c:forEach var="meetup" items="${joinedMeetups}">
                        <div class="mini-card mb-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <strong>${meetup.title}</strong>
                                <span class="badge text-bg-light">${meetup.meetupType}</span>
                            </div>
                            <div class="text-muted small mt-2">${meetup.location}</div>
                            <a href="${pageContext.request.contextPath}/meetups?action=detail&id=${meetup.id}" class="btn btn-sm btn-outline-primary mt-2">Open</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h4 class="mb-3">Meetups you created</h4>
                    <c:if test="${empty myMeetups}">
                        <p class="text-muted mb-0">You have not created any meetup yet.</p>
                    </c:if>
                    <c:forEach var="meetup" items="${myMeetups}">
                        <div class="mini-card mb-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <strong>${meetup.title}</strong>
                                <span class="badge text-bg-primary">${meetup.meetupType}</span>
                            </div>
                            <div class="text-muted small mt-2">${meetup.location}</div>
                            <a href="${pageContext.request.contextPath}/meetups?action=detail&id=${meetup.id}" class="btn btn-sm btn-outline-primary mt-2">Manage</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
