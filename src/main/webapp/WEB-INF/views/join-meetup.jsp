<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Meetup</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/dashboard">Meetup</a>
        <div class="ms-auto">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-light btn-sm">Dashboard</a>
        </div>
    </div>
</nav>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card border-0 shadow-sm">
                <div class="card-body p-4 p-md-5">
                    <h2 class="mb-4">Create a new meetup</h2>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/meetups" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="create">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label">Meetup type</label>
                                <select name="meetupType" class="form-select" required>
                                    <option value="Solo">Solo</option>
                                    <option value="Group">Group</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Category</label>
                                <select name="category" class="form-select" required>
                                    <option value="Social">Social</option>
                                    <option value="Study">Study</option>
                                    <option value="Sports">Sports</option>
                                    <option value="Food">Food</option>
                                    <option value="Tech">Tech</option>
                                    <option value="Travel">Travel</option>
                                </select>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Title</label>
                                <input type="text" name="title" class="form-control" maxlength="160" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Description</label>
                                <textarea name="description" class="form-control" rows="4" required></textarea>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Location</label>
                                <input type="text" name="location" class="form-control" maxlength="180" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Date and time</label>
                                <input type="datetime-local" name="eventDateTime" class="form-control" required>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Upload image</label>
                                <input type="file" name="image" class="form-control" accept="image/*">
                            </div>
                        </div>
                        <div class="d-flex justify-content-between mt-4">
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary">Publish meetup</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
