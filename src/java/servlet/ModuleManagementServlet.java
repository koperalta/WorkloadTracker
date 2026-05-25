package servlet;

import dao.AuditDAO;
import dao.WorkloadDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ModuleManagementServlet extends HttpServlet {

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
                String title = request.getParameter("title");

                workloadDAO.createModule(adminId, title);
                auditDAO.logAdminAction(adminId, "CREATED MODULE TITLE \"" + title + "\"");

            } else if ("edit".equals(action)) {
                int moduleId = Integer.parseInt(request.getParameter("moduleId"));
                String title = request.getParameter("title");

                workloadDAO.updateModule(moduleId, title);
                auditDAO.logAdminAction(adminId, "UPDATED MODULE ID " + moduleId + " TO NEW TITLE \"" + title + "\"");

            } else if ("delete".equals(action)) {
                int moduleId = Integer.parseInt(request.getParameter("moduleId"));

                workloadDAO.deleteModule(moduleId);
                auditDAO.logAdminAction(adminId, "DELETED MODULE ID " + moduleId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
    }
}