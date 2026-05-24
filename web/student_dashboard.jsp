<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="#" class="logo">
            💡 Active<span>Learning</span>
        </a>
        <div style="font-size: 14px; display: flex; gap: 20px; align-items: center;">
            <span>Welcome, <strong>Student</strong></span>
            <a href="${pageContext.request.contextPath}/LogoutServlet" style="color: var(--brand-orange); text-decoration: none; font-weight: bold;">Logout</a>
        </div>
    </nav>

    <div class="dashboard-container">

        <div class="dashboard-card">
            <h3>My Active Training Tasks</h3>
            <p style="margin-bottom: 15px; color: #555;">View your assigned modules and track your progress.</p>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Module ID</th>
                        <th>Task Title</th>
                        <th>Current Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="taskItem" items="${workloadData}">
                        <tr>
                            <td>MOD-${taskItem.task.moduleId}</td>
                            <td>${taskItem.task.title}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${taskItem.status == 'Completed'}">
                                        <span style="color: blue; font-weight: bold;">${taskItem.status}</span>
                                    </c:when>
                                    <c:when test="${taskItem.status == 'In Progress'}">
                                        <span style="color: green; font-weight: bold;">${taskItem.status}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: orange; font-weight: bold;">${taskItem.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn-submit" style="padding: 5px 10px; font-size: 12px; width: auto;">Log Time</button>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty workloadData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888;">You have no active tasks at the moment.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>