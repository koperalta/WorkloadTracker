package workloadtracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IdentityDAO {

    // Authenticates the user and returns their USER_ID if successful, or -1 if it fails
    public int authenticateUser(String username, String passwordHash) {
        int userId = -1;
        String sql = "SELECT USER_ID FROM DERBY_USERS WHERE USERNAME = ? AND PASSWORD_HASH = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getDerbyConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, passwordHash); // In a production environment, use bcrypt/argon2 comparison
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("USER_ID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // In Phase 4, exceptions here should bubble up or route to the 500 error page
        }
        return userId;
    }

    // Retrieves the role assigned to a specific user for authorization routing
    public String getUserRole(int userId) {
        String roleName = null;
        String sql = "SELECT r.ROLE_NAME FROM DERBY_ROLES r " +
                     "JOIN DERBY_USER_ROLES ur ON r.ROLE_ID = ur.ROLE_ID " +
                     "WHERE ur.USER_ID = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getDerbyConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roleName = rs.getString("ROLE_NAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleName;
    }
}