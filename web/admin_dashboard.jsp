<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - ActiveLearning</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="#" class="logo">
            💡 Active<span>Learning</span>
        </a>
        <div style="font-size: 14px; display: flex; gap: 30px; align-items: center;">
            
            <span style="color: #ddd; font-weight: normal;">
                Welcome, <strong style="color: white;"><c:out value="${sessionScope.adminName}" default="Administrator"/></strong>
            </span>

            <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
        </div>
    </nav>

    <div class="dashboard-container">
        
        <div class="dashboard-card">
            <h3>System Report Generation</h3>
            <p style="margin-bottom: 25px; color: #666; font-size: 15px;">Download official workload and audit reports from the PostgreSQL Database.</p>
            
            <div class="report-actions">
                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="all_tasks">
                    <button type="submit" class="btn-report">Download All User Tasks</button>
                </form>

                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="audit_logs">
                    <button type="submit" class="btn-report">Download Full Audit Trail</button>
                </form>

                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="my_audit_logs">
                    <button type="submit" class="btn-report" style="background-color: var(--brand-navy);">Download My Records Only</button>
                </form>
            </div>

            <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET" class="time-bound-box">
                <h4 style="margin-top: 0; margin-bottom: 15px; color: var(--brand-navy);">Time-Bound Audit Report</h4>
                <div style="display: flex; gap: 20px; align-items: flex-end;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>Start Date</label>
                        <input type="date" name="startDate" required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label>End Date</label>
                        <input type="date" name="endDate" required>
                    </div>
                    <button type="submit" class="btn-submit">Generate Custom Report</button>
                </div>
            </form>
        </div>

        <div class="dashboard-card">
            <h3>Recent System Activity (Audit Trail)</h3>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Audit ID</th>
                        <th>Admin ID</th>
                        <th>Action Performed</th>
                        <th>Timestamp</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${auditLogsData}">
                        <tr>
                            <td><strong>#${log.auditId}</strong></td>
                            <td>Admin ${log.adminId}</td>
                            <td style="font-family: monospace; color: var(--brand-orange); font-weight: bold;">${log.actionType}</td>
                            <td>${log.actionTimestamp}</td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty auditLogsData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888; padding: 30px;">No recent audit logs found in PostgreSQL.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div class="dashboard-card">
            <h3>User Login History (Session Logs)</h3>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Log ID</th>
                        <th>User ID</th>
                        <th>Login Time</th>
                        <th>Logout Time</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="sessionLog" items="${sessionLogsData}">
                        <tr>
                            <td><strong>#${sessionLog.logId}</strong></td>
                            <td>User ${sessionLog.userId}</td>
                            <td>${sessionLog.loginTime}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty sessionLog.logoutTime}">
                                        ${sessionLog.logoutTime}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: green; font-weight: bold;">Active Session</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty sessionLogsData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888; padding: 30px;">No session records found.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>