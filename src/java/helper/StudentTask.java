package helper;


public class StudentTask {
    private int assignmentId;
    private int taskId;
    private int userId;
    private String status;
    private String username;
    private Task task; // Useful for displaying task details on the JSP

    public StudentTask() {}

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
    
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}