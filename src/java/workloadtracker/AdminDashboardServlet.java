package workloadtracker;

package workloadtracker;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/AdminDashboard"})
public class AdminDashboardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Session and Security Verification
        HttpSession session = request.getSession(false);
        
        // Check if session exists and if the user is authenticated
        if (session == null || session.getAttribute("userRole") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
            return;
        }

        // Enforce Strict Admin Access
        String userRole = (String) session.getAttribute("userRole");
        if (!userRole.equals("ADMIN")) {
            // If a regular user tries to access this, boot them to their correct dashboard or an error page
            response.sendRedirect(request.getContextPath() + "/FetchWorkload");
            return;
        }

        // Retrieve the specific Admin ID from the session
        int adminId = (int) session.getAttribute("userId");

        try {
            // 2. Data Retrieval (MySQL - Business Logic & State)
            // Retrieve modules and tasks specifically owned by this admin
            WorkloadDAO workloadDAO = new WorkloadDAO();
            List<TrainingModule> adminModules = workloadDAO.getModulesByAdminId(adminId);
            List<Task> adminTasks = workloadDAO.getTasksByAdminId(adminId);
            
            // 3. Data Retrieval (PostgreSQL - Time-Series & Audit Logging)
            // Retrieve recent actions performed by this admin for the dashboard ledger
            AuditDAO auditDAO = new AuditDAO();
            List<AuditLog> recentLogs = auditDAO.getAuditLogsByAdminId(adminId);

            // 4. Bind Data to Request Attributes
            request.setAttribute("adminModules", adminModules);
            request.setAttribute("adminTasks", adminTasks);
            request.setAttribute("recentLogs", recentLogs);
            request.setAttribute("adminId", adminId);

            // 5. Forward to Presentation Tier
            request.getRequestDispatcher("/admin_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            // Phase 4 Usability Rubric: Custom error routing instead of stack traces
            e.printStackTrace(); // Log to server console for debugging
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