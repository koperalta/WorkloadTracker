package workloadtracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkloadDAO {
    
    public List<StudentTask> getTasksForStudent(int userId) throws SQLException, Exception {
        List<StudentTask> studentTasks = new ArrayList<>();
        String sql = "SELECT st.ASSIGNMENT_ID, st.TASK_ID, st.USER_ID, st.STATUS, " +
                     "t.MODULE_ID, t.TITLE " +
                     "FROM MYSQL_STUDENT_TASKS st " +
                     "JOIN MYSQL_TASKS t ON st.TASK_ID = t.TASK_ID " +
                     "WHERE st.USER_ID = ?";

        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
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
        
        try (Connection conn = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, newStatus);
            stmt.setInt(2, assignmentId);
            stmt.executeUpdate();
        }
    }
}