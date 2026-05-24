package dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class IdentityDAO {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    // 1. New Constructor: Captures XML parameters and loads the Derby driver
    public IdentityDAO(ServletContext context) throws ClassNotFoundException {
        this.dbUrl = context.getInitParameter("derbyURL");
        this.dbUser = context.getInitParameter("derbyUsername");
        this.dbPass = context.getInitParameter("derbyPassword");
        String dbDriver = context.getInitParameter("derbyDriver");

        // Load the Derby client driver into memory
        Class.forName(dbDriver);
    }

    // 2. Helper Method: Opens the connection using DriverManager
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    // Authenticates the user and returns their USER_ID if successful, or -1 if it fails
    public int authenticateUser(String username, String passwordHash) {
        int userId = -1;
        String sql = "SELECT USER_ID FROM DERBY_USERS WHERE USERNAME = ? AND PASSWORD_HASH = ?";

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
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

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
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
    
    public int getUserIdByUsername(String username) {
        int userId = -1;
        String sql = "SELECT USER_ID FROM DERBY_USERS WHERE USERNAME = ?";

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("USER_ID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
}