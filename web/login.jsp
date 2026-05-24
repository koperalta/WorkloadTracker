<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>System Login - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="#" class="logo">
            💡 Active<span>Learning</span>
        </a>
        <div class="tagline">
            IT and Project Management Training
        </div>
    </nav>

    <div class="login-container">
        <h2>System Login</h2>
        
        <form action="${pageContext.request.contextPath}/LoginServlet" method="POST">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" placeholder="e.g. admin" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            
            <div class="captcha-box">
                <label>Security CAPTCHA</label>
                <p style="font-size: 12px; color: #666; margin-bottom: 10px;">Please enter the text shown below.</p>
                <img src="${pageContext.request.contextPath}/CaptchaServlet" alt="CAPTCHA Image">
                <input type="text" name="captcha" placeholder="Enter the text above" required>
            </div>
            
            <button type="submit" class="btn-submit">Secure Login</button>
        </form>
    </div>

</body>
</html>