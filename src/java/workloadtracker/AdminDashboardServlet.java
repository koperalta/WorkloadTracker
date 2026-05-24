package workloadtracker;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/AdminDashboardServlet"})
public class AdminDashboardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Session and Security Verification
        HttpSession session = request.getSession(false);
        
        // Look for your teammate's specific session key
        if (session == null || session.getAttribute("loggedUserRoleId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        // Enforce Strict Admin Access based on their integer logic
        int roleId = (int) session.getAttribute("loggedUserRoleId");
        if (roleId == 2) {
            // Role 2 is a student. Boot them to the workload fetcher.
            response.sendRedirect(request.getContextPath() + "/FetchWorkloadServlet");
            return;
        }

        // 2. ID Translation
        // Retrieve the Integer ID directly, as LoginServlet now stores it as an Integer
        Integer adminId = (Integer) session.getAttribute("loggedUserId");

        try {
            // If you need to perform actions based on adminId, you have it as an Integer directly
    
            // 3. Data Retrieval (MySQL - Business Logic & State)
            WorkloadDAO workloadDAO = new WorkloadDAO(getServletContext());
            List<TrainingModule> adminModules = workloadDAO.getModulesByAdminId(adminId);
            List<Task> adminTasks = workloadDAO.getTasksByAdminId(adminId);
    
            // 4. Data Retrieval (PostgreSQL - Time-Series & Audit Logging)
            AuditDAO auditDAO = new AuditDAO(getServletContext());
            List<AuditLog> recentLogs = auditDAO.getAuditLogsByAdminId(adminId);

            // 5. Bind Data to Request Attributes
            request.setAttribute("adminModules", adminModules);
            request.setAttribute("adminTasks", adminTasks);
            request.setAttribute("auditLogsData", recentLogs);
            request.setAttribute("adminId", adminId);

            // 6. Forward to Presentation Tier
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading the admin dashboard: " + e.getMessage());
            request.getRequestDispatcher("/error500.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller for the Admin Dashboard handling MySQL modules and PostgreSQL audit logs";
    }
}