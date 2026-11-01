<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notifications</title>
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
    <div class="card border-0 shadow-sm">
        <div class="card-body p-4">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Notifications</h2>
                <form method="post" action="${pageContext.request.contextPath}/notifications">
                    <input type="hidden" name="action" value="mark-all-read">
                    <button type="submit" class="btn btn-outline-primary btn-sm">Mark all as read</button>
                </form>
            </div>

            <c:if test="${empty notifications}">
                <p class="text-muted mb-0">No notifications yet.</p>
            </c:if>

            <div class="list-group">
                <c:forEach var="notification" items="${notifications}">
                    <div class="list-group-item d-flex justify-content-between align-items-center ${notification.read ? '' : 'fw-semibold'}">
                        <span>${notification.message}</span>
                        <form method="post" action="${pageContext.request.contextPath}/notifications">
                            <input type="hidden" name="action" value="mark-read">
                            <input type="hidden" name="id" value="${notification.id}">
                            <button type="submit" class="btn btn-sm ${notification.read ? 'btn-outline-secondary' : 'btn-primary'}">
                                ${notification.read ? 'Read' : 'Mark read'}
                            </button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</body>
</html>
