package servlet;

import dao.AuditDAO;
import dao.WorkloadDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AssignmentManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Verify session security and get the active Admin ID
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUserRoleId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        Integer adminId = (Integer) session.getAttribute("loggedUserId");
        String action = request.getParameter("action");

        try {
            // Instantiate DAOs using the servlet context parameters
            WorkloadDAO workloadDAO = new WorkloadDAO(getServletContext());
            AuditDAO auditDAO = new AuditDAO(getServletContext());

            // 2. Handle CREATE (Assign task)
            if ("create".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                String status = request.getParameter("status");

                // Execute change in MySQL workload database
                workloadDAO.assignTaskToStudent(userId, taskId, status);
                
                // Write trace to PostgreSQL Audit Trail
                String auditMessage = "ASSIGNED TASK ID " + taskId + " TO USER ID " + userId + " WITH STATUS: " + status;
                auditDAO.logAdminAction(adminId, auditMessage);

            // 3. Handle EDIT (Update assignment status)
            } else if ("edit".equals(action)) {
                int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
                String status = request.getParameter("status");

                // Execute change in MySQL workload database
                workloadDAO.updateTaskStatus(assignmentId, status);
                
                // Write trace to PostgreSQL Audit Trail
                String auditMessage = "UPDATED ASSIGNMENT ID " + assignmentId + " TO STATUS: " + status;
                auditDAO.logAdminAction(adminId, auditMessage);

            // 4. Handle DELETE (Remove assignment)
            } else if ("delete".equals(action)) {
                int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));

                // Execute change in MySQL workload database
                workloadDAO.deleteTaskAssignment(assignmentId);
                
                // Write trace to PostgreSQL Audit Trail
                String auditMessage = "DELETED ASSIGNMENT ID " + assignmentId;
                auditDAO.logAdminAction(adminId, auditMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 5. Redirect back to your dashboard controller to show the updated lists and logs
        response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
    }
}