<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Workload Dashboard - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="#" class="logo">
            💡 Active<span>Learning</span>
        </a>
        <div style="font-size: 14px; display: flex; gap: 20px; align-items: center;">
            <span>Welcome, <strong>Admin*</strong></span>
            <a href="${pageContext.request.contextPath}/LogoutServlet" style="color: var(--brand-orange); text-decoration: none; font-weight: bold;">Logout</a>
        </div>
    </nav>

    <div class="dashboard-container">
        
        <div class="dashboard-card" style="border-top-color: var(--brand-orange);">
            <h3>PDF Report Generation</h3>
            <p style="margin-bottom: 15px; color: #555;">Download official workload and user reports. Files will download directly to your device.</p>
            
            <div class="report-actions">
                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="all">
                    <button type="submit" class="btn-report">Download ALL Records</button>
                </form>

                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="personal">
                    <button type="submit" class="btn-report">Download MY Records</button>
                </form>
            </div>

            <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET" style="margin-top: 20px; background: #f9f9f9; padding: 15px; border-radius: 4px; border: 1px dashed #ccc;">
                <h4 style="margin-bottom: 10px;">Time-Bound Report</h4>
                <div style="display: flex; gap: 15px; align-items: flex-end;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>Start Date</label>
                        <input type="date" name="startDate" required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>End Date</label>
                        <input type="date" name="endDate" required>
                    </div>
                    <button type="submit" class="btn-submit" style="width: auto;">Generate Custom Report</button>
                </div>
            </form>
        </div>

        <div class="dashboard-card">
            <h3>Active Training Tasks</h3>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Module ID</th>
                        <th>Task Title</th>
                        <th>Assigned To</th>
                        <th>Current Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="taskItem" items="${workloadData}">
                        <tr>
                            <td>MOD-${taskItem.task.moduleId}</td>
                            <td>${taskItem.task.title}</td>
                            <td>Student ID: ${taskItem.userId}</td>
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
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty workloadData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888;">No active tasks found in the database.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>