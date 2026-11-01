<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Join Meetup</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">Meetup</a>
        <div class="ms-auto d-flex gap-2">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-light btn-sm">Dashboard</a>
        </div>
    </div>
</nav>

<div class="container py-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0">Available meetups</h2>
    </div>

    <div class="row g-4">
        <c:forEach var="meetup" items="${meetups}">
            <div class="col-lg-4 col-md-6">
                <div class="card h-100 shadow-sm border-0">
                    <c:if test="${not empty meetup.imagePath}">
                        <img src="${pageContext.request.contextPath}/${meetup.imagePath}" class="card-img-top meetup-image" alt="${meetup.title}">
                    </c:if>
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <h5 class="card-title mb-0">${meetup.title}</h5>
                            <span class="badge text-bg-primary">${meetup.meetupType}</span>
                        </div>
                        <div class="text-muted small mb-2">${meetup.category}</div>
                        <p class="small text-muted mb-2">${meetup.location}</p>
                        <p class="small text-muted mb-3">
                            <strong>Date:</strong> ${meetup.eventDateTime}<br>
                            <strong>Organizer:</strong> ${meetup.organizerName}
                        </p>
                        <div class="d-flex justify-content-between align-items-center">
                            <span class="small text-muted">${meetup.participantCount} members</span>
                            <div>
                                <a href="${pageContext.request.contextPath}/meetups?action=detail&id=${meetup.id}" class="btn btn-outline-primary btn-sm">Details</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
