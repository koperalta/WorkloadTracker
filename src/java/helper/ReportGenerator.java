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
    
    // Generates the legacy user list (Derby)
    public static void generateAllRecords(OutputStream os, String currentUser, ServletContext context) throws Exception {
        
        // Constraint: Landscape Mode
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
        
        // Retrieve Derby connection details from web.xml
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

                // Constraint: Put an asterisk (*) beside the logged-in admin
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
    
    // Generates the time-bound audit report (PostgreSQL)
    public static void generateTimeBoundReport(OutputStream os, String currentUser, int adminId, String startDate, String endDate, ServletContext context) throws Exception {
        
        // Constraint: Landscape Mode
        Document doc = new Document(PageSize.LETTER.rotate(), 36, 36, 60, 50);
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
        
        // Retrieve PostgreSQL connection details from web.xml
        String url = context.getInitParameter("postgresqlURL");
        String user = context.getInitParameter("postgresqlUsername");
        String pass = context.getInitParameter("postgresqlPassword");
        Class.forName(context.getInitParameter("postgresqlDriver"));
        
        // Constraint: SQL BETWEEN clauses for time-bound isolation
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
}