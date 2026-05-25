package helper;


public class AuditLog {
    private int auditId;
    private int adminId;
    private String actionType;
    private String actionTimestamp;

    // Getters and Setters
    public int getAuditId() { return auditId; }
    public void setAuditId(int auditId) { this.auditId = auditId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getActionTimestamp() { return actionTimestamp; }
    public void setActionTimestamp(String actionTimestamp) { this.actionTimestamp = actionTimestamp; }
}
