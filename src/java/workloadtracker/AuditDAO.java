package workloadtracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class AuditDAO {

    // Inserts a new immutable record into the audit ledger
    public void logAdminAction(int adminId, String actionType) {
        String sql = "INSERT INTO POSTGRES_AUDIT_LOGS (ADMIN_ID, ACTION_TYPE, ACTION_TIMESTAMP) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnectionUtil.getInstance().getPostgresConnection();
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

        try (Connection conn = DBConnectionUtil.getInstance().getPostgresConnection();
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