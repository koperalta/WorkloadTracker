<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="workloadtracker.TrainingModule"%>
<%@page import="workloadtracker.Task"%>
<%@page import="workloadtracker.AuditLog"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Active Learning</title>
    <!-- Links to your teammate's existing stylesheet -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* Supplementary styles specifically for the admin layout tables */
        .admin-container { 
            padding: 2% 5%; 
            font-family: Arial, sans-serif; 
        }
        .admin-header { 
            background-color: #f37021; /* Active Learning Orange */
            color: white; 
            padding: 15px 5%; 
            display: flex; 
            justify-content: space-between; 
            align-items: center; 
        }
        .admin-header h1 { margin: 0; font-size: 1.5em; }
        .logout-btn { 
            background-color: white; 
            color: #f37021; 
            text-decoration: none; 
            padding: 8px 15px; 
            border-radius: 4px; 
            font-weight: bold; 
        }
        .data-table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 15px; 
            background: white; 
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .data-table th, .data-table td { 
            border: 1px solid #ddd; 
            padding: 12px; 
            text-align: left; 
        }
        .data-table th { background-color: #f4f4f4; color: #333; }
        .section-title { 
            margin-top: 40px; 
            border-bottom: 2px solid #f37021; 
            padding-bottom: 5px; 
            color: #333;
        }
    </style>
</head>
<body>
    <!-- Top Navigation Bar -->
    <div class="admin-header">
        <h1>💡 Active Learning - Admin Portal</h1>
        <a href="${pageContext.request.contextPath}/login.jsp" class="logout-btn">Logout</a>
    </div>

    <div class="admin-container">
        <!-- Pulls the Admin ID from the session attribute set in your servlet -->
        <h2>Welcome, Administrator (ID: <%= request.getAttribute("adminId") %>)</h2>
        <p>This is your secure control panel for curriculum management and immutable audit logs.</p>

        <!-- Section 1: MySQL Training Modules -->
        <h3 class="section-title">📚 Your Managed Modules</h3>
        <table class="data-table">
            <tr>
                <th>Module ID</th>
                <th>Title</th>
            </tr>
            <% 
                // Retrieves the list of modules passed by the AdminDashboardServlet
                List<TrainingModule> modules = (List<TrainingModule>) request.getAttribute("adminModules");
                if (modules != null && !modules.isEmpty()) {
                    for (TrainingModule m : modules) {
            %>
            <tr>
                <td><%= m.getModuleId() %></td>
                <td><%= m.getTitle() %></td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr><td colspan="2">No modules found. Ensure database is populated.</td></tr>
            <% } %>
        </table>

        <!-- Section 2: MySQL Tasks -->
        <h3 class="section-title">📋 Your Managed Tasks</h3>
        <table class="data-table">
            <tr>
                <th>Task ID</th>
                <th>Module ID</th>
                <th>Task Title</th>
            </tr>
            <% 
                // Retrieves the list of tasks passed by the AdminDashboardServlet
                List<Task> tasks = (List<Task>) request.getAttribute("adminTasks");
                if (tasks != null && !tasks.isEmpty()) {
                    for (Task t : tasks) {
            %>
            <tr>
                <td><%= t.getTaskId() %></td>
                <td><%= t.getModuleId() %></td>
                <td><%= t.getTitle() %></td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr><td colspan="3">No tasks found. Ensure database is populated.</td></tr>
            <% } %>
        </table>

        <!-- Section 3: PostgreSQL Audit Logs -->
        <h3 class="section-title">🔒 PostgreSQL Audit Ledger</h3>
        <table class="data-table">
            <tr>
                <th>Audit ID</th>
                <th>Action Type</th>
                <th>Timestamp</th>
            </tr>
            <% 
                // Retrieves the time-series logs passed by the AdminDashboardServlet
                List<AuditLog> logs = (List<AuditLog>) request.getAttribute("recentLogs");
                if (logs != null && !logs.isEmpty()) {
                    for (AuditLog log : logs) {
            %>
            <tr>
                <td><%= log.getAuditId() %></td>
                <td><%= log.getActionType() %></td>
                <td><%= log.getActionTimestamp() %></td>
            </tr>
            <% 
                    }
                } else { 
            %>
            <tr><td colspan="3">No recent audit logs available. Try performing an administrative action.</td></tr>
            <% } %>
        </table>
    </div>
</body>
</html>