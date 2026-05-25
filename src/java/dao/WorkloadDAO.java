package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import helper.StudentTask;
import helper.Task;
import helper.TrainingModule;

public class WorkloadDAO {

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public WorkloadDAO(ServletContext context) throws ClassNotFoundException {
        this.dbUrl = context.getInitParameter("mysqlDBurl");
        this.dbUser = context.getInitParameter("mysqlDBusername");
        this.dbPass = context.getInitParameter("mysqlDBpassword");
        String dbDriver = context.getInitParameter("mysqlDBdriver");

        Class.forName(dbDriver);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public List<StudentTask> getTasksForStudent(int userId) throws SQLException, Exception {
        List<StudentTask> studentTasks = new ArrayList<>();
        String sql = "SELECT st.ASSIGNMENT_ID, st.TASK_ID, st.USER_ID, st.STATUS, "
                + "t.MODULE_ID, t.TITLE "
                + "FROM MYSQL_STUDENT_TASKS st "
                + "JOIN MYSQL_TASKS t ON st.TASK_ID = t.TASK_ID "
                + "WHERE st.USER_ID = ?";

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
                    module.setTitle(rs.getString("TITLE"));
                    modules.add(module);
                }
            }
        }
        return modules;
    }

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
                    task.setTitle(rs.getString("TITLE"));
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    public void assignTaskToStudent(int userId, int taskId, String status) throws SQLException {
        String sql = "INSERT INTO MYSQL_STUDENT_TASKS (USER_ID, TASK_ID, STATUS) VALUES (?, ?, ?)";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, taskId);
            stmt.setString(3, status);
            stmt.executeUpdate();
        }
    }

    public void deleteTaskAssignment(int assignmentId) throws SQLException {
        String sql = "DELETE FROM MYSQL_STUDENT_TASKS WHERE ASSIGNMENT_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, assignmentId);
            stmt.executeUpdate();
        }
    }

    public List<StudentTask> getAllStudentTasks() throws SQLException, Exception {
        List<StudentTask> studentTasks = new ArrayList<>();
        String sql = "SELECT st.ASSIGNMENT_ID, st.TASK_ID, st.USER_ID, st.STATUS, t.TITLE "
                + "FROM MYSQL_STUDENT_TASKS st "
                + "JOIN MYSQL_TASKS t ON st.TASK_ID = t.TASK_ID "
                + "ORDER BY st.ASSIGNMENT_ID DESC";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StudentTask studentTask = new StudentTask();
                studentTask.setAssignmentId(rs.getInt("ASSIGNMENT_ID"));
                studentTask.setTaskId(rs.getInt("TASK_ID"));
                studentTask.setUserId(rs.getInt("USER_ID"));
                studentTask.setStatus(rs.getString("STATUS"));

                Task task = new Task();
                task.setTaskId(rs.getInt("TASK_ID"));
                task.setTitle(rs.getString("TITLE"));

                studentTask.setTask(task);
                studentTasks.add(studentTask);
            }
        }
        return studentTasks;
    }

    public void createTask(int moduleId, int adminId, String title) throws SQLException {
        String sql = "INSERT INTO MYSQL_TASKS (MODULE_ID, ADMIN_ID, TITLE) VALUES (?, ?, ?)";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduleId);
            stmt.setInt(2, adminId);
            stmt.setString(3, title);
            stmt.executeUpdate();
        }
    }

    public void updateTask(int taskId, int moduleId, String title) throws SQLException {
        String sql = "UPDATE MYSQL_TASKS SET MODULE_ID = ?, TITLE = ? WHERE TASK_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduleId);
            stmt.setString(2, title);
            stmt.setInt(3, taskId);
            stmt.executeUpdate();
        }
    }

    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM MYSQL_TASKS WHERE TASK_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        }
    }

    public void createModule(int adminId, String title) throws SQLException {
        String sql = "INSERT INTO MYSQL_TRAINING_MODULES (ADMIN_ID, TITLE) VALUES (?, ?)";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, adminId);
            stmt.setString(2, title);
            stmt.executeUpdate();
        }
    }

    public void updateModule(int moduleId, String title) throws SQLException {
        String sql = "UPDATE MYSQL_TRAINING_MODULES SET TITLE = ? WHERE MODULE_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setInt(2, moduleId);
            stmt.executeUpdate();
        }
    }

    public void deleteModule(int moduleId) throws SQLException {
        String sql = "DELETE FROM MYSQL_TRAINING_MODULES WHERE MODULE_ID = ?";

        try (Connection conn = this.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduleId);
            stmt.executeUpdate();
        }
    }
}
