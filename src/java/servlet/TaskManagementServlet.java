package servlet;

import dao.AuditDAO;
import dao.WorkloadDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TaskManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUserRoleId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        Integer adminId = (Integer) session.getAttribute("loggedUserId");
        String action = request.getParameter("action");

        try {
            WorkloadDAO workloadDAO = new WorkloadDAO(getServletContext());
            AuditDAO auditDAO = new AuditDAO(getServletContext());

            if ("create".equals(action)) {
                int moduleId = Integer.parseInt(request.getParameter("moduleId"));
                String title = request.getParameter("title");

                workloadDAO.createTask(moduleId, adminId, title);
                auditDAO.logAdminAction(adminId, "CREATED TASK \"" + title + "\" IN MODULE " + moduleId);

            } else if ("edit".equals(action)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                int moduleId = Integer.parseInt(request.getParameter("moduleId"));
                String title = request.getParameter("title");

                workloadDAO.updateTask(taskId, moduleId, title);
                auditDAO.logAdminAction(adminId, "UPDATED TASK ID " + taskId + " TO NEW TITLE \"" + title + "\"");

            } else if ("delete".equals(action)) {
                int taskId = Integer.parseInt(request.getParameter("taskId"));

                workloadDAO.deleteTask(taskId);
                auditDAO.logAdminAction(adminId, "DELETED TASK ID " + taskId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
    }
}