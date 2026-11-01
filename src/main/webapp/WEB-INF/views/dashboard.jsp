<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${pageType == 'login'}">Meetup Login</c:when><c:otherwise>Meetup Register</c:otherwise></c:choose></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="auth-bg">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
            <div class="card auth-card shadow-sm border-0">
                <div class="card-body p-4 p-md-5">
                    <div class="text-center mb-4">
                        <h1 class="brand-title">Meetup</h1>
                        <p class="text-muted">
                            <c:choose>
                                <c:when test="${pageType == 'login'}">Welcome back</c:when>
                                <c:otherwise>Join your local community</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <c:choose>
                        <c:when test="${pageType == 'login'}">
                            <form method="post" action="${pageContext.request.contextPath}/auth">
                                <input type="hidden" name="action" value="login">
                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email" name="email" class="form-control" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Password</label>
                                    <input type="password" name="password" class="form-control" required>
                                </div>
                                <button type="submit" class="btn btn-primary w-100">Login</button>
                            </form>
                            <div class="text-center mt-3">
                                <span class="text-muted">Need an account?</span>
                                <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">Sign up</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <form method="post" action="${pageContext.request.contextPath}/auth">
                                <input type="hidden" name="action" value="register">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Full name</label>
                                        <input type="text" name="name" class="form-control" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">City</label>
                                        <input type="text" name="city" class="form-control" required>
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label">Email</label>
                                        <input type="email" name="email" class="form-control" required>
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label">Password</label>
                                        <input type="password" name="password" class="form-control" minlength="6" required>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary w-100 mt-4">Register</button>
                            </form>
                            <div class="text-center mt-3">
                                <span class="text-muted">Already have an account?</span>
                                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none">Login</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
