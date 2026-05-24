<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Page Not Found - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .easter-egg-box { position: relative; overflow: hidden; }
        .tiger-watermark {
            position: absolute; bottom: -20px; right: -20px;
            font-size: 150px; opacity: 0.05; user-select: none; transform: rotate(-15deg);
        }
        .funny-quote { font-style: italic; color: #888; font-size: 14px; margin-bottom: 30px; }
    </style>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/login.jsp" class="logo">
            💡 Active<span>Learning</span>
        </a>
    </nav>

    <div class="dashboard-container easter-egg-box" style="text-align: center; margin-top: 100px;">
        <div class="tiger-watermark">🐅</div>
        <h1 style="font-size: 80px; color: var(--brand-orange);">404</h1>
        <h2 style="color: var(--brand-dark);">Oops! We cannot find that page.</h2>
        <p style="margin-top: 15px; margin-bottom: 10px; font-size: 18px;">
            The page you are looking for might have been removed or is temporarily unavailable.
        </p>
        <p class="funny-quote">"Lost to the void. Just like that last star I needed to hit Mythical Immortal..."</p>
        <a href="${pageContext.request.contextPath}/FetchWorkloadServlet" class="btn-submit" style="display: inline-block; width: auto; text-decoration: none; padding: 12px 30px;">Return to Dashboard</a>
    </div>
</body>
</html>