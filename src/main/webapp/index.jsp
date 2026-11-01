<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meetup App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-lg border-0">
                <div class="card-body p-4">
                    <div class="text-center mb-4">
                        <h1 class="brand-title">Meetup</h1>
                        <p class="text-muted">Find People, Create Plans and Meet Together</p>
                    </div>
                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">Login</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-primary btn-lg">Create an account</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
