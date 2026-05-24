<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Server Error - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: calc(100vh - 150px);
        }
        .error-card {
            position: relative;
            overflow: hidden;
            text-align: center;
            padding: 60px 40px;
            max-width: 600px;
            width: 100%;
            background: white;
            border-radius: 8px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            border-top: 5px solid #d9534f;
        }
        .tiger-watermark {
            position: absolute;
            bottom: -30px;
            right: -30px;
            font-size: 180px;
            opacity: 0.03;
            user-select: none;
            transform: rotate(-15deg);
            z-index: 0;
        }
        .error-content {
            position: relative;
            z-index: 1;
        }
        .funny-quote {
            font-style: italic;
            color: #999;
            font-size: 14px;
            margin: 25px 0 35px 0;
        }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/login.jsp" class="logo">
            💡 Active<span>Learning</span>
        </a>
    </nav>

    <div class="dashboard-container error-wrapper">
        <div class="error-card">
            
            <div class="tiger-watermark">🐅</div>
            
            <div class="error-content">
                <h1 style="font-size: 90px; color: #d9534f; line-height: 1; margin-bottom: 10px; font-weight: bold;">500</h1>
                <h2 style="color: var(--brand-navy); font-size: 26px; margin-bottom: 15px;">Internal Server Error</h2>
                <p style="color: #666; font-size: 16px; line-height: 1.6;">
                    Something went wrong on our databases. Our engineering team has been notified and is working to restore functionality.
                </p>
                <p class="funny-quote">"Is this the real life? Is this just fantasy? Caught in a server crash, no escape from reality..."</p>
                
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn-submit" style="display: inline-block; width: auto; text-decoration: none; padding: 12px 35px; border-radius: 4px;">Return to Login</a>
            </div>
            
        </div>
    </div>

</body>
</html>