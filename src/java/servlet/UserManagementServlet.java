package servlet;

import dao.AuditDAO;
import dao.IdentityDAO;
import helper.Encrypter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUserRoleId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String key = getServletContext().getInitParameter("secretKey");
        String instance = getServletContext().getInitParameter("cipherInstance");
        int adminId = (int) session.getAttribute("loggedUserId");
        String action = request.getParameter("action");

        try {
            IdentityDAO identityDAO = new IdentityDAO(getServletContext());
            AuditDAO auditDAO = new AuditDAO(getServletContext());

            if ("create".equals(action)) {
                String username = request.getParameter("username");
                String email = request.getParameter("email"); // Extract email from request string parameter payload
                String passwordRaw = request.getParameter("password");
                int roleId = Integer.parseInt(request.getParameter("roleId"));

                String passwordHash = Encrypter.encrypt(passwordRaw, key, instance);

                // Pass email as a direct parameter argument
                identityDAO.createUserWithRole(username, email, passwordHash, roleId);
                auditDAO.logAdminAction(adminId, "IDENTITY USER CREATED: " + username + " (" + email + ") WITH ROLE ID: " + roleId);
            } else if ("edit".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int roleId = Integer.parseInt(request.getParameter("roleId"));
                String newPasswordRaw = request.getParameter("password");

                // In production, insert your hashing logic layer here (e.g., SHA-256)
                String newPasswordHash = Encrypter.encrypt(newPasswordRaw, key, instance);

                // Trigger the combined transaction method update
                identityDAO.updateUserProfile(userId, newPasswordHash, roleId);
                auditDAO.logAdminAction(adminId, "IDENTITY USER ID " + userId + " UPDATED: PASSWORD CHANGED & ROLE CHANGED TO " + roleId);
            } else if ("delete".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String targetUsername = identityDAO.getUsernameById(userId);

                identityDAO.deleteUser(userId);
                auditDAO.logAdminAction(adminId, "IDENTITY USER DELETED: " + targetUsername + " (ID: #" + userId + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/AdminDashboardServlet");
    }
}
