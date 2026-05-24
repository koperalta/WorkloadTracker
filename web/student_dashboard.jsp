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
        <div style="font-size: 14px; display: flex; gap: 30px; align-items: center;">
            
            <span style="color: #ddd; font-weight: normal;">
                Welcome, <strong style="color: white;"><c:out value="${sessionScope.studentName}" default="Student"/></strong>
            </span>

            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
        </div>
    </nav>

    <div class="dashboard-container">

        <div class="dashboard-card">
            <h3>My Active Training Tasks</h3>
            <p style="margin-bottom: 20px; color: #666; font-size: 15px;">View your assigned modules and track your progress.</p>
            
            <div class="report-actions" style="margin-bottom: 25px;">
                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="my_student_tasks">
                    <button type="submit" class="btn-report">Download My Task Report</button>
                </form>
            </div>

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
                            <td><strong>MOD-${taskItem.task.moduleId}</strong></td>
                            <td style="color: var(--brand-navy); font-weight: 500;">${taskItem.task.title}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${taskItem.status == 'Completed'}">
                                        <span style="color: #0056b3; font-weight: bold; background: #e6f2ff; padding: 4px 8px; border-radius: 4px; font-size: 13px;">${taskItem.status}</span>
                                    </c:when>
                                    <c:when test="${taskItem.status == 'In Progress'}">
                                        <span style="color: #1e7e34; font-weight: bold; background: #e6ffed; padding: 4px 8px; border-radius: 4px; font-size: 13px;">${taskItem.status}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #d39e00; font-weight: bold; background: #fff8e6; padding: 4px 8px; border-radius: 4px; font-size: 13px;">${taskItem.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button class="btn-submit" style="padding: 8px 16px; font-size: 13px;">Log Time</button>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty workloadData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888; padding: 40px;">You have no active tasks at the moment. Enjoy your break!</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>