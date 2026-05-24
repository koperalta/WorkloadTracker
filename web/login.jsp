<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>System Login - ActiveLearning</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <script src="https://www.google.com/recaptcha/api.js" asynch defer></script>
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
                <%
                    String error = (String) request.getAttribute("errorMessage");
                    if (error != null) {
                %>
                <div style="color: red; margin-bottom: 10px;">
                    <%= error%>
                </div>
                <%
                    }
                %>
                
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="e.g. admin" required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                </div>
                <br/><br/>
                

                <div class="g-recaptcha" data-sitekey="6LdUXrwsAAAAAFKnPzdaNzqDd8lvmXs11o6BIv7w" style="margin-bottom: 15px;"></div>

                <button type="submit" class="btn-submit">Secure Login</button>
            </form>
        </div>

    </body>
</html>