<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${meetup.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">Meetup</a>
        <div class="ms-auto d-flex gap-2">
            <a href="${pageContext.request.contextPath}/meetups" class="btn btn-light btn-sm">Back</a>
        </div>
    </div>
</nav>

<div class="container py-5">
    <div class="row g-4">
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm">
                <c:if test="${not empty meetup.imagePath}">
                    <img src="${pageContext.request.contextPath}/${meetup.imagePath}" class="card-img-top detail-image" alt="${meetup.title}">
                </c:if>
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2 class="mb-0">${meetup.title}</h2>
                        <span class="badge text-bg-primary">${meetup.meetupType}</span>
                    </div>
                    <p class="text-muted">${meetup.category}</p>
                    <p>${meetup.description}</p>
                    <ul class="list-unstyled detail-list">
                        <li><strong>Location:</strong> ${meetup.location}</li>
                        <li><strong>Date:</strong> ${meetup.eventDateTime}</li>
                        <li><strong>Organizer:</strong> ${meetup.organizerName}</li>
                    </ul>

                    <div class="d-flex gap-2 flex-wrap mt-3">
                        <c:if test="${!joined}">
                            <form method="post" action="${pageContext.request.contextPath}/meetups">
                                <input type="hidden" name="action" value="join">
                                <input type="hidden" name="id" value="${meetup.id}">
                                <button class="btn btn-primary">Join meetup</button>
                            </form>
                        </c:if>
                        <c:if test="${joined}">
                            <form method="post" action="${pageContext.request.contextPath}/meetups">
                                <input type="hidden" name="action" value="leave">
                                <input type="hidden" name="id" value="${meetup.id}">
                                <button class="btn btn-outline-danger">Leave meetup</button>
                            </form>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/chat?meetupId=${meetup.id}" class="btn btn-outline-primary">Chat</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <h4>Participants</h4>
                    <ul class="list-group list-group-flush">
                        <c:forEach var="participant" items="${participants}">
                            <li class="list-group-item">${participant.userName}</li>
                        </c:forEach>
                    </ul>

                    <c:if test="${sessionScope.user.id == meetup.organizerId}">
                        <div class="mt-4">
                            <form method="post" action="${pageContext.request.contextPath}/meetups">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${meetup.id}">
                                <button class="btn btn-danger w-100">Delete meetup</button>
                            </form>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
