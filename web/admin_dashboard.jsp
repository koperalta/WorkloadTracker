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
        <div style="font-size: 14px; display: flex; gap: 20px; align-items: center;">
            <span style="color: var(--brand-orange); font-weight: bold;">Administrator View</span>
            <a href="${pageContext.request.contextPath}/LogoutServlet" style="color: var(--brand-dark); text-decoration: none; font-weight: bold;">Logout</a>
        </div>
    </nav>

    <div class="dashboard-container">
        
        <div class="dashboard-card" style="border-top-color: var(--brand-orange);">
            <h3>System Report Generation</h3>
            <p style="margin-bottom: 15px; color: #555;">Download official workload and audit reports from the PostgreSQL Database.</p>
            
            <div class="report-actions">
                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="all_tasks">
                    <button type="submit" class="btn-report">Download All User Tasks</button>
                </form>

                <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                    <input type="hidden" name="type" value="audit_logs">
                    <button type="submit" class="btn-report">Download Full Audit Trail</button>
                </form>
            </div>

            <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET" style="margin-top: 20px; background: #f9f9f9; padding: 15px; border-radius: 4px; border: 1px dashed #ccc;">
                <h4 style="margin-bottom: 10px;">Time-Bound Audit Report</h4>
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
                            <td>${log.auditId}</td>
                            <td>Admin ${log.adminId}</td>
                            <td style="font-family: monospace; color: #333;">${log.actionType}</td>
                            <td>${log.actionTimestamp}</td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty auditLogsData}">
                        <tr>
                            <td colspan="4" style="text-align: center; font-style: italic; color: #888;">No recent audit logs found in PostgreSQL.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </div>
</body>
</html>