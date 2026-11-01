<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat - ${meetup.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="chat-body">
<div class="container py-4">
    <div class="chat-shell card border-0 shadow-sm">
        <div class="chat-header d-flex justify-content-between align-items-center">
            <div>
                <h4 class="mb-0">${meetup.title}</h4>
                <small>${meetup.location}</small>
            </div>
            <a href="${pageContext.request.contextPath}/meetups?action=detail&id=${meetup.id}" class="btn btn-sm btn-outline-light">Back</a>
        </div>

        <div class="chat-window" id="chatWindow">
            <c:forEach var="message" items="${messages}">
                <div class="chat-message">
                    <div class="chat-author">${message.senderName}</div>
                    <div class="chat-bubble">${message.content}</div>
                    <small class="chat-time">${message.createdAt}</small>
                </div>
            </c:forEach>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/chat" class="chat-form">
            <input type="hidden" name="meetupId" value="${meetup.id}">
            <div class="input-group">
                <input type="text" name="content" class="form-control" placeholder="Write a message..." required>
                <button type="submit" class="btn btn-primary">Send</button>
            </div>
        </form>
    </div>
</div>

<script>
    const chatWindow = document.getElementById('chatWindow');
    const meetupId = '${meetup.id}';
    setInterval(function() {
        fetch('${pageContext.request.contextPath}/chat?meetupId=' + meetupId + '&format=json')
            .then(response => response.json())
            .then(data => {
                const messages = data.messages || [];
                if (!messages.length) return;
                const current = chatWindow.innerHTML;
                let html = '';
                for (const msg of messages) {
                    html += '<div class="chat-message"><div class="chat-author">' + msg.senderName + '</div><div class="chat-bubble">' + msg.content + '</div><small class="chat-time">' + msg.createdAt + '</small></div>';
                }
                chatWindow.innerHTML = html;
                chatWindow.scrollTop = chatWindow.scrollHeight;
            })
            .catch(() => {});
    }, 3000);
</script>
</body>
</html>
