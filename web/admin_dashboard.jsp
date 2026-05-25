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
                        <input type="hidden" name="type" value="all_users">
                        <button type="submit" class="btn-report">Download User List</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                        <input type="hidden" name="type" value="audit_logs">
                        <button type="submit" class="btn-report">Download Full Audit Trail</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/GenerateReportServlet" method="GET">
                        <input type="hidden" name="type" value="my_audit_logs">
                        <button type="submit" class="btn-report" style="background-color: var(--brand-navy);">Download My Audit Trail</button>
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
                <h3>Current Student Module Assignments (Assignment ID Details)</h3>
                <div style="max-height: 250px; overflow-y: auto; margin-bottom: 25px; border: 1px solid #ddd; border-radius: 4px;">
                    <table class="data-table" style="margin: 0; width: 100%;">
                        <thead>
                            <tr>
                                <th>Assignment ID</th>
                                <th>User ID</th>
                                <th>Task ID / Title</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="asg" items="${allAssignments}">
                                <tr>
                                    <td><strong>#${asg.assignmentId}</strong></td>
                                    <td>User ${asg.userId}</td>
                                    <td>(${asg.taskId}) ${asg.task.title}</td>
                                    <td style="font-weight: bold; color: var(--brand-orange);">${asg.status}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty allAssignments}">
                                <tr>
                                    <td colspan="4" style="text-align: center; font-style: italic; color: #888; padding: 20px;">No student assignments found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <h4 style="margin-top: 0; margin-bottom: 15px; color: var(--brand-navy);">Manage Assignments</h4>
                <div style="display: flex; gap: 20px; flex-wrap: wrap;">
                    <form action="${pageContext.request.contextPath}/AssignmentManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="create">
                        <h5 style="margin: 0 0 10px 0;">Assign New Task</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Existing Student (User)</label>
                            <select name="userId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Student --</option>
                                <c:forEach var="student" items="${uniqueStudents}">
                                    <option value="${student.userId}">${student.username} (ID: #${student.userId})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Existing Task</label>
                            <select name="taskId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Task --</option>
                                <c:forEach var="task" items="${uniqueTasks}">
                                    <option value="${task.taskId}">${task.title} (ID: #${task.taskId})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Status</label>
                            <select name="status" style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="Pending">PENDING</option>
                                <option value="In Progress">IN_PROGRESS</option>
                                <option value="Completed">COMPLETED</option>
                            </select>
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px;">Assign Task</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/AssignmentManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="edit">
                        <h5 style="margin: 0 0 10px 0;">Update Existing Assignment</h5>
                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Assignment</label>
                            <select name="assignmentId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Assignment --</option>
                                <c:forEach var="asg" items="${allAssignments}">
                                    <option value="${asg.assignmentId}">ID: #${asg.assignmentId} (User ${asg.userId} - ${asg.status})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">New Status</label>
                            <select name="status" style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="Pending">PENDING</option>
                                <option value="In Progress">IN_PROGRESS</option>
                                <option value="Completed">COMPLETED</option>
                            </select>
                        </div>
                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: var(--brand-orange);">Update Status</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/AssignmentManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #fdf2f2; padding: 15px; border-radius: 4px; border: 1px solid #f5c6cb;">
                        <input type="hidden" name="action" value="delete">
                        <h5 style="margin: 0 0 10px 0; color: #721c24;">Remove Assignment</h5>
                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Assignment to Delete</label>
                            <select name="assignmentId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Assignment --</option>
                                <c:forEach var="asg" items="${allAssignments}">
                                    <option value="${asg.assignmentId}">ID: #${asg.assignmentId} (User ${asg.userId} - ${asg.task.title})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: #dc3545;" onclick="return confirm('Are you sure you want to delete this student task assignment?');">Delete Assignment</button>
                    </form>
                </div>
            </div>
            <div class="dashboard-card">
                <h3>System Module Registry Management</h3>

                <div style="display: flex; gap: 20px; flex-wrap: wrap; margin-bottom: 25px;">

                    <form action="${pageContext.request.contextPath}/ModuleManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="create">
                        <h5 style="margin: 0 0 10px 0;">Define New Module</h5>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Module Title</label>
                            <input type="text" name="title" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px;">Create Base Module</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/ModuleManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="edit">
                        <h5 style="margin: 0 0 10px 0;">Modify Existing Module</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Target Module</label>
                            <select name="moduleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Module --</option>
                                <c:forEach var="module" items="${allModules}">
                                    <option value="${module.moduleId}">
                                        <c:out value="${module.title}"/> (ID: #${module.moduleId})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">New Module Title</label>
                            <input type="text" name="title" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: var(--brand-orange);">Update Definition</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/ModuleManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #fdf2f2; padding: 15px; border-radius: 4px; border: 1px solid #f5c6cb;">
                        <input type="hidden" name="action" value="delete">
                        <h5 style="margin: 0 0 10px 0; color: #721c24;">Purge Module Registry</h5>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Target Module</label>
                            <select name="moduleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Module --</option>
                                <c:forEach var="module" items="${allModules}">
                                    <option value="${module.moduleId}">
                                        <c:out value="${module.title}"/> (ID: #${module.moduleId})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: #dc3545;" onclick="return confirm('Warning: Deleting this structural module layout will cascade delete dependent tasks and student metrics. Proceed?');">Delete Module Permanently</button>
                    </form>
                </div>
            </div>
            <div class="dashboard-card">
                <h3>System Task Registry Management</h3>

                <div style="display: flex; gap: 20px; flex-wrap: wrap; margin-bottom: 25px;">

                    <form action="${pageContext.request.contextPath}/TaskManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="create">
                        <h5 style="margin: 0 0 10px 0;">Define New Task</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Parent Module</label>
                            <select name="moduleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Module --</option>
                                <c:forEach var="module" items="${allModules}">
                                    <option value="${module.moduleId}">${module.title} (ID: #${module.moduleId})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Task Title</label>
                            <input type="text" name="title" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px;">Create Base Task</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/TaskManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="edit">
                        <h5 style="margin: 0 0 10px 0;">Modify Existing Task</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Task</label>
                            <select name="taskId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Task --</option>
                                <c:forEach var="tsk" items="${allGlobalTasks}">
                                    <option value="${tsk.taskId}">${tsk.title} (ID: #${tsk.taskId})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Change Parent Module</label>
                            <select name="moduleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Module --</option>
                                <c:forEach var="module" items="${allModules}">
                                    <option value="${module.moduleId}">
                                        <c:out value="${module.title}" default="Unnamed Module"/> (ID: #${module.moduleId})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">New Task Title</label>
                            <input type="text" name="title" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: var(--brand-orange);">Update Definition</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/TaskManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #fdf2f2; padding: 15px; border-radius: 4px; border: 1px solid #f5c6cb;">
                        <input type="hidden" name="action" value="delete">
                        <h5 style="margin: 0 0 10px 0; color: #721c24;">Purge Task Registry</h5>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Target Task</label>
                            <select name="taskId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Task --</option>
                                <c:forEach var="tsk" items="${allGlobalTasks}">
                                    <option value="${tsk.taskId}">${tsk.title} (ID: #${tsk.taskId})</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: #dc3545;" onclick="return confirm('Warning: Deleting this structural task pattern will cascade delete dependencies on student history tracks. Proceed?');">Delete Task</button>
                    </form>
                </div>
            </div>
            <div class="dashboard-card">
                <h3>System User Account Management</h3>
                <div style="display: flex; gap: 20px; flex-wrap: wrap; margin-bottom: 25px;">

                    <form action="${pageContext.request.contextPath}/UserManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="create">
                        <h5 style="margin: 0 0 10px 0;">Register New Account</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Username</label>
                            <input type="text" name="username" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Email Address</label>
                            <input type="email" name="email" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Password</label>
                            <input type="password" name="password" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Assigned Role Title</label>
                            <select name="roleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="1">ADMINISTRATOR (Role #1)</option>
                                <option value="2">STUDENT (Role #2)</option>
                            </select>
                        </div>

                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px;">Provision Account</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/UserManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #f9f9f9; padding: 15px; border-radius: 4px;">
                        <input type="hidden" name="action" value="edit">
                        <h5 style="margin: 0 0 10px 0;">Modify Account Profile</h5>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Account Profile</label>
                            <select name="userId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Account --</option>
                                <c:forEach var="usr" items="${globalSystemUsers}">
                                    <option value="${usr.userId}">
                                        <c:out value="${usr.username}"/> (Current: ${usr.status})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group" style="margin-bottom: 10px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Set New Password</label>
                            <input type="password" name="password" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                        </div>

                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select New Role Title</label>
                            <select name="roleId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="1">ADMINISTRATOR (Role #1)</option>
                                <option value="2">STUDENT (Role #2)</option>
                            </select>
                        </div>
                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: var(--brand-orange);">Update Authorization</button>
                    </form>

                    <form action="${pageContext.request.contextPath}/UserManagementServlet" method="POST" style="flex: 1; min-width: 250px; background: #fdf2f2; padding: 15px; border-radius: 4px; border: 1px solid #f5c6cb;">
                        <input type="hidden" name="action" value="delete">
                        <h5 style="margin: 0 0 10px 0; color: #721c24;">Purge User Profile</h5>
                        <div class="form-group" style="margin-bottom: 12px;">
                            <label style="display: block; font-size: 12px; margin-bottom: 4px;">Select Account to Purge</label>
                            <select name="userId" required style="width: 100%; padding: 6px; box-sizing: border-box;">
                                <option value="">-- Choose Account --</option>
                                <c:forEach var="usr" items="${globalSystemUsers}">
                                    <option value="${usr.userId}">${usr.username} (ID: #${usr.userId})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn-submit" style="width: 100%; padding: 8px; background-color: #dc3545;" onclick="return confirm('Warning: Deleting this account will instantly terminate authorization access tokens. Proceed?');">Delete Account Permanently</button>
                    </form>
                </div>
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
        </div>
    </body>
</html>