package workloadtracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import javax.servlet.ServletContext;

public class AuditDAO {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    // 1. New Constructor: Captures XML parameters and loads the PostgreSQL driver
    public AuditDAO(ServletContext context) throws ClassNotFoundException {
        this.dbUrl = context.getInitParameter("postgresqlURL");
        this.dbUser = context.getInitParameter("postgresqlUsername");
        this.dbPass = context.getInitParameter("postgresqlPassword");
        String dbDriver = context.getInitParameter("postgresqlDriver");

        // Load the PostgreSQL driver into memory
        Class.forName(dbDriver);
    }

    // 2. Helper Method: Opens the connection using DriverManager
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    // Inserts a new immutable record into the audit ledger
    public void logAdminAction(int adminId, String actionType) {
        String sql = "INSERT INTO POSTGRES_AUDIT_LOGS (ADMIN_ID, ACTION_TYPE, ACTION_TIMESTAMP) VALUES (?, ?, CURRENT_TIMESTAMP)";

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminId);
            stmt.setString(2, actionType);
            stmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Retrieves the ledger of actions for a specific admin to display on their dashboard
    public List<AuditLog> getAuditLogsByAdminId(int adminId) {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT AUDIT_ID, ADMIN_ID, ACTION_TYPE, ACTION_TIMESTAMP " +
                     "FROM POSTGRES_AUDIT_LOGS WHERE ADMIN_ID = ? ORDER BY ACTION_TIMESTAMP DESC";

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setAuditId(rs.getInt("AUDIT_ID"));
                    log.setAdminId(rs.getInt("ADMIN_ID"));
                    log.setActionType(rs.getString("ACTION_TYPE"));
                    log.setActionTimestamp(rs.getTimestamp("ACTION_TIMESTAMP"));
                    logs.add(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}