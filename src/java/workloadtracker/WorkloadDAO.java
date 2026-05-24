package workloadtracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;

public class WorkloadDAO {
    
    private String dbUrl;
    private String dbUser;
    private String dbPass;

    // 1. New Constructor: Captures XML parameters and loads the driver
    public WorkloadDAO(ServletContext context) throws ClassNotFoundException {
        this.dbUrl = context.getInitParameter("mysqlDBurl");
        this.dbUser = context.getInitParameter("mysqlDBuser");
        this.dbPass = context.getInitParameter("mysqlDBpassword");
        String dbDriver = context.getInitParameter("mysqlDBdriver");

        // Load the MySQL driver into memory
        Class.forName(dbDriver);
    }

    // 2. Helper Method: Opens the connection using DriverManager
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
    
    public List<StudentTask> getTasksForStudent(int userId) throws SQLException, Exception {
        List<StudentTask> studentTasks = new ArrayList<>();
        String sql = "SELECT st.ASSIGNMENT_ID, st.TASK_ID, st.USER_ID, st.STATUS, " +
                     "t.MODULE_ID, t.TITLE " +
                     "FROM MYSQL_STUDENT_TASKS st " +
                     "JOIN MYSQL_TASKS t ON st.TASK_ID = t.TASK_ID " +
                     "WHERE st.USER_ID = ?";

        // 3. Replaced DBConnectionUtil with this.getConnection()
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    StudentTask studentTask = new StudentTask();
                    studentTask.setAssignmentId(rs.getInt("ASSIGNMENT_ID"));
                    studentTask.setTaskId(rs.getInt("TASK_ID"));
                    studentTask.setUserId(rs.getInt("USER_ID"));
                    studentTask.setStatus(rs.getString("STATUS"));
                    
                    Task task = new Task();
                    task.setTaskId(rs.getInt("TASK_ID"));
                    task.setModuleId(rs.getInt("MODULE_ID"));
                    task.setTitle(rs.getString("TITLE"));
                    
                    studentTask.setTask(task);
                    studentTasks.add(studentTask);
                }
            }
        }
        return studentTasks;
    }
    
    public void updateTaskStatus(int assignmentId, String newStatus) throws SQLException, Exception {
        String sql = "UPDATE MYSQL_STUDENT_TASKS SET STATUS = ? WHERE ASSIGNMENT_ID = ?";
        
        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, newStatus);
            stmt.setInt(2, assignmentId);
            stmt.executeUpdate();
        }
    }
    
    // Retrieves all training modules created by a specific admin
    public List<TrainingModule> getModulesByAdminId(int adminId) throws SQLException, Exception {
        List<TrainingModule> modules = new ArrayList<>();
        String sql = "SELECT MODULE_ID, ADMIN_ID, TITLE FROM MYSQL_TRAINING_MODULES WHERE ADMIN_ID = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, adminId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TrainingModule module = new TrainingModule();
                    module.setModuleId(rs.getInt("MODULE_ID"));
                    // module.setAdminId(rs.getInt("ADMIN_ID")); // Uncomment if TrainingModule has this setter
                    module.setTitle(rs.getString("TITLE"));
                    modules.add(module);
                }
            }
        }
        return modules;
    }

    // Retrieves all tasks created by a specific admin
    public List<Task> getTasksByAdminId(int adminId) throws SQLException, Exception {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT TASK_ID, MODULE_ID, ADMIN_ID, TITLE FROM MYSQL_TASKS WHERE ADMIN_ID = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, adminId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task();
                    task.setTaskId(rs.getInt("TASK_ID"));
                    task.setModuleId(rs.getInt("MODULE_ID"));
                    // task.setAdminId(rs.getInt("ADMIN_ID")); // Uncomment if Task has this setter
                    task.setTitle(rs.getString("TITLE"));
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }
}