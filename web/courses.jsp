<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IT Learning Paths - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/login.jsp" class="logo">
            💡 Active<span>Learning</span>
        </a>
        <div style="display: flex; align-items: center;">
            <div class="tagline" style="margin-right: 25px;">
                IT and Project Management Training
            </div>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn-logout">Student Portal Login</a>
        </div>
    </nav>

    <div class="learning-section">
        <h2>Learn where to start and what to take next<br>with our popular <span style="color: var(--brand-orange); font-weight: bold;">IT learning paths</span></h2>
        
        <div class="path-grid">
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="AI" class="path-image">
                <div class="path-content"><div class="path-title">AI and Process Automation</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="ITIL" class="path-image">
                <div class="path-content"><div class="path-title">ITIL</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Cybersecurity" class="path-image">
                <div class="path-content"><div class="path-title">Cybersecurity</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="CompTIA" class="path-image">
                <div class="path-content"><div class="path-title">CompTIA</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Python" class="path-image">
                <div class="path-content"><div class="path-title">Python Programming</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Project Management" class="path-image">
                <div class="path-content"><div class="path-title">Project Management</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Data Analysis" class="path-image">
                <div class="path-content"><div class="path-title">Data Analysis</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Excel" class="path-image">
                <div class="path-content"><div class="path-title">Microsoft Excel</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Azure" class="path-image">
                <div class="path-content"><div class="path-title">Microsoft Azure</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="UX" class="path-image">
                <div class="path-content"><div class="path-title">UX</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Java" class="path-image">
                <div class="path-content"><div class="path-title">Java</div></div>
            </a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="path-card">
                <img src="" alt="Web Development" class="path-image">
                <div class="path-content"><div class="path-title">Web Development</div></div>
            </a>
        </div>
    </div>

</body>
</html>