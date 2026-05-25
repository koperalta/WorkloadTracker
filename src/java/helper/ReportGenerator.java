package helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletContext;

public class ReportGenerator {
    
    public static void generateAllRecords(OutputStream os, String currentUser, ServletContext context) throws Exception {
        
        Document doc = new Document(PageSize.LETTER.rotate(), 36, 36, 120, 50);
        PdfWriter write = PdfWriter.getInstance(doc, os); 
        
        String header = context.getInitParameter("pdfHeader");
        String footer = context.getInitParameter("pdfFooter");
        
        PageEventHelper eventHelper = new PageEventHelper("ALL SYSTEM RECORDS", currentUser, header, footer);
        write.setPageEvent(eventHelper);
        doc.open();
        
        
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        doc.add(new Paragraph("Exact Time Generated: " + timeStamp, normalFont));
        doc.add(new Paragraph(" ")); // Spacer
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        
        table.addCell(new PdfPCell(new Phrase("Username", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Role", boldFont)));
        
        String url = context.getInitParameter("derbyURL");
        String user = context.getInitParameter("derbyUsername");
        String pass = context.getInitParameter("derbyPassword");
        Class.forName(context.getInitParameter("derbyDriver"));
        
        String query = "SELECT u.USERNAME, r.ROLE_NAME FROM DERBY_USERS u " +
                       "JOIN DERBY_USER_ROLES ur ON u.USER_ID = ur.USER_ID " +
                       "JOIN DERBY_ROLES r ON ur.ROLE_ID = r.ROLE_ID";
                       
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String dbUser = rs.getString("USERNAME");
                String dbRole = rs.getString("ROLE_NAME");

                String displayUsername = dbUser;
                if (dbUser != null && dbUser.equalsIgnoreCase(currentUser)) {
                    displayUsername += " *";
                }

                table.addCell(new PdfPCell(new Phrase(displayUsername, normalFont)));
                table.addCell(new PdfPCell(new Phrase(dbRole, normalFont)));
            }
        }
        
        doc.add(table);
        doc.close();
    }
    
    public static void generateTimeBoundReport(OutputStream os, String currentUser, int adminId, String startDate, String endDate, ServletContext context) throws Exception {
        
        Document doc = new Document(PageSize.LETTER.rotate(), 36, 36, 120, 50);
        PdfWriter write = PdfWriter.getInstance(doc, os); 
        
        String header = context.getInitParameter("pdfHeader");
        String footer = context.getInitParameter("pdfFooter");
        
        String title = "MY RECORDS: " + (startDate != null ? startDate + " to " + endDate : "Complete Trail");
        PageEventHelper eventHelper = new PageEventHelper(title, currentUser, header, footer);
        write.setPageEvent(eventHelper);
        doc.open();
        
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        doc.add(new Paragraph("Exact Time Generated: " + timeStamp, normalFont));
        doc.add(new Paragraph(" ")); 
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        
        table.addCell(new PdfPCell(new Phrase("Audit ID", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Action Performed", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Timestamp", boldFont)));
        
        String url = context.getInitParameter("postgresqlURL");
        String user = context.getInitParameter("postgresqlUsername");
        String pass = context.getInitParameter("postgresqlPassword");
        Class.forName(context.getInitParameter("postgresqlDriver"));
        
        String query;
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            query = "SELECT AUDIT_ID, ACTION_TYPE, ACTION_TIMESTAMP FROM POSTGRES_AUDIT_LOGS " +
                    "WHERE ADMIN_ID = ? AND ACTION_TIMESTAMP BETWEEN ?::timestamp AND ?::timestamp " +
                    "ORDER BY ACTION_TIMESTAMP DESC";
        } else {
            query = "SELECT AUDIT_ID, ACTION_TYPE, ACTION_TIMESTAMP FROM POSTGRES_AUDIT_LOGS " +
                    "WHERE ADMIN_ID = ? ORDER BY ACTION_TIMESTAMP DESC";
        }
        
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query)) {
             
             ps.setInt(1, adminId);
             if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
                 ps.setString(2, startDate + " 00:00:00");
                 ps.setString(3, endDate + " 23:59:59");
             }
             
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(rs.getInt("AUDIT_ID")), normalFont)));
                    table.addCell(new PdfPCell(new Phrase(rs.getString("ACTION_TYPE"), normalFont)));
                    table.addCell(new PdfPCell(new Phrase(rs.getString("ACTION_TIMESTAMP"), normalFont)));
                }
            }
        }
        
        doc.add(table);
        doc.close();
    }
    
    public static void generateStudentTaskReport(OutputStream os, String currentUser, int studentId, ServletContext context) throws Exception {
        
        Document doc = new Document(PageSize.LETTER.rotate(), 36, 36, 120, 50);
        PdfWriter write = PdfWriter.getInstance(doc, os); 
        
        String header = context.getInitParameter("pdfHeader");
        String footer = context.getInitParameter("pdfFooter");
        
        PageEventHelper eventHelper = new PageEventHelper("MY ACTIVE TRAINING TASKS", currentUser, header, footer);
        write.setPageEvent(eventHelper);
        doc.open();
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        
        table.addCell(new PdfPCell(new Phrase("Module ID", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Task Title", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Current Status", boldFont)));
        
        String url = context.getInitParameter("mysqlDBurl");
        String user = context.getInitParameter("mysqlDBusername");
        String pass = context.getInitParameter("mysqlDBpassword");
        Class.forName(context.getInitParameter("mysqlDBdriver"));
        
        String query = "SELECT t.MODULE_ID, t.TITLE, st.STATUS " +
                       "FROM mysql_student_tasks st " +
                       "JOIN mysql_tasks t ON st.TASK_ID = t.TASK_ID " +
                       "WHERE st.USER_ID = ?";
                       
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(query)) {
             
             ps.setInt(1, studentId);
             
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String modId = "MOD-" + rs.getInt("MODULE_ID");
                    String title = rs.getString("TITLE");
                    String status = rs.getString("STATUS");

                    table.addCell(new PdfPCell(new Phrase(modId, normalFont)));
                    table.addCell(new PdfPCell(new Phrase(title, normalFont)));
                    
                    // Formatting the status cell based on completion
                    PdfPCell statusCell = new PdfPCell(new Phrase(status, normalFont));
                    if ("Completed".equalsIgnoreCase(status)) {
                        statusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    }
                    table.addCell(statusCell);
                }
            }
        }
        
        doc.add(table);
        doc.close();
    }
}