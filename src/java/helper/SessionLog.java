package helper;

import java.sql.Timestamp;

public class SessionLog {
    private int logId;
    private int userId;
    private Timestamp loginTime;
    private Timestamp logoutTime;

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getLoginTime() { return loginTime; }
    public void setLoginTime(Timestamp loginTime) { this.loginTime = loginTime; }

    public Timestamp getLogoutTime() { return logoutTime; }
    public void setLogoutTime(Timestamp logoutTime) { this.logoutTime = logoutTime; }
}