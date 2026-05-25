package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        String sql = "SELECT r.ROLE_NAME FROM DERBY_ROLES r "
                + "JOIN DERBY_USER_ROLES ur ON r.ROLE_ID = ur.ROLE_ID "
                + "WHERE ur.USER_ID = ?";

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

    public String getUsernameById(int userId) {
        String username = null;
        String sql = "SELECT USERNAME FROM DERBY_USERS WHERE USER_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("USERNAME");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    // Fixes user creation to use standard driver key retrieval on DERBY_USERS
    public void createUserWithRole(String username, String email, String passwordHash, int roleId) throws SQLException {
        String insertUserSql = "INSERT INTO DERBY_USERS (USERNAME, EMAIL, PASSWORD_HASH) VALUES (?, ?, ?)";
        String identityLookupSql = "VALUES IDENTITY_VAL_LOCAL()";
        String insertRoleSql = "INSERT INTO DERBY_USER_ROLES (USER_ID, ROLE_ID) VALUES (?, ?)";

        try (Connection conn = this.getConnection()) {
            conn.setAutoCommit(false); // Enable atomic transaction control

            int generatedUserId = -1;

            // 1. Write the base identity record
            try (PreparedStatement stmt = conn.prepareStatement(insertUserSql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, passwordHash);
                stmt.executeUpdate();
            }

            // 2. Fetch the ID that Derby generated for this specific execution thread
            try (PreparedStatement stmt = conn.prepareStatement(identityLookupSql);
                    ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    generatedUserId = rs.getInt(1);
                }
            }

            // 3. Link the new ID to the selected Role ID
            if (generatedUserId > 0) {
                try (PreparedStatement roleStmt = conn.prepareStatement(insertRoleSql)) {
                    roleStmt.setInt(1, generatedUserId);
                    roleStmt.setInt(2, roleId);
                    roleStmt.executeUpdate();
                }
                conn.commit(); // Success! Flush both rows safely together
            } else {
                conn.rollback();
                throw new SQLException("Database rejection: IDENTITY_VAL_LOCAL() returned an invalid ID.");
            }

        } catch (SQLException e) {
            System.err.println("CRITICAL IDENTITY REGISTER FAILURE: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

// Fixes user profile updates using only your existing tables
    public void updateUserProfile(int userId, String newPasswordHash, int newRoleId) throws SQLException {
        String updateUserSql = "UPDATE DERBY_USERS SET PASSWORD_HASH = ? WHERE USER_ID = ?";
        String updateRoleSql = "UPDATE DERBY_USER_ROLES SET ROLE_ID = ? WHERE USER_ID = ?";

        try (Connection conn = this.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(updateUserSql)) {
                    stmt.setString(1, newPasswordHash);
                    stmt.setInt(2, userId);
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(updateRoleSql)) {
                    stmt.setInt(1, newRoleId);
                    stmt.setInt(2, userId);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

// Fixes user deletion to clear rows cleanly from your two tables
    public void deleteUser(int userId) throws SQLException {
        String deleteRoleLinkSql = "DELETE FROM DERBY_USER_ROLES WHERE USER_ID = ?";
        String deleteUserSql = "DELETE FROM DERBY_USERS WHERE USER_ID = ?";

        try (Connection conn = this.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(deleteRoleLinkSql)) {
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement(deleteUserSql)) {
                    stmt.setInt(1, userId);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

// FIX: Pulls all students (ROLE_ID = 2) without trying to join a non-existent table
    public List<helper.StudentTask> getAllStudentsFromIdentityRegistry() {
        List<helper.StudentTask> students = new ArrayList<>();
        String sql = "SELECT u.USER_ID, u.USERNAME FROM DERBY_USERS u "
                + "JOIN DERBY_USER_ROLES ur ON u.USER_ID = ur.USER_ID "
                + "WHERE ur.ROLE_ID = 2 ORDER BY u.USER_ID ASC";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                helper.StudentTask student = new helper.StudentTask();
                student.setUserId(rs.getInt("USER_ID"));
                student.setUsername(rs.getString("USERNAME"));
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

// FIX: Pulls all users and translates ROLE_ID to text inline via SQL CASE
    public List<helper.StudentTask> getAllSystemUsersRegistry() {
        List<helper.StudentTask> users = new ArrayList<>();
        // Direct, unjoined select query to guarantee rows are returned from DERBY_USERS
        String sql = "SELECT USER_ID, USERNAME FROM DERBY_USERS ORDER BY USER_ID ASC";
        String roleSql = "SELECT ROLE_ID FROM DERBY_USER_ROLES WHERE USER_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                helper.StudentTask wrapper = new helper.StudentTask();
                int userId = rs.getInt("USER_ID");
                wrapper.setUserId(userId);
                wrapper.setUsername(rs.getString("USERNAME"));

                // Inline role extraction to safely build the label string
                String roleName = "UNKNOWN";
                try (PreparedStatement roleStmt = conn.prepareStatement(roleSql)) {
                    roleStmt.setInt(1, userId);
                    try (ResultSet roleRs = roleStmt.executeQuery()) {
                        if (roleRs.next()) {
                            int roleId = roleRs.getInt("ROLE_ID");
                            roleName = (roleId == 1) ? "ADMINISTRATOR" : "STUDENT";
                        }
                    }
                }

                wrapper.setStatus(roleName); // Stored temporarily for display styling
                users.add(wrapper);
            }
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR FETCHING ALL SYSTEM USERS: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
