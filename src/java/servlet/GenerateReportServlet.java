package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import helper.ReportGenerator;

public class GenerateReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUserId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String currentUser = (String) session.getAttribute("loggedUsername");
        int userId = (Integer) session.getAttribute("loggedUserId");

        String reportType = request.getParameter("type");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Constraint: Dynamically generate timestamped filename
        String fileTimestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "";

        if ("all_users".equals(reportType)) {
            fileName = "USER-LIST_" + fileTimestamp + ".pdf";
        } else if ("audit_logs".equals(reportType)) {
            fileName = "ALL-LOGS_" + fileTimestamp + ".pdf";
        } else if ("my_audit_logs".equals(reportType)) {
            fileName = "MY-LOGS_" + fileTimestamp + ".pdf";
        } else if ("my_student_tasks".equals(reportType)) {
            fileName = "MY-TASKS_" + fileTimestamp + ".pdf";
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try {
            if ("all_users".equals(reportType)) {
                ReportGenerator.generateAllUsers(response.getOutputStream(), currentUser, getServletContext());
            } else if ("audit_logs".equals(reportType)) {
                ReportGenerator.generateAllRecords(response.getOutputStream(), currentUser, getServletContext());
            } else if ("my_student_tasks".equals(reportType)) {
                ReportGenerator.generateStudentTaskReport(response.getOutputStream(), currentUser, userId, getServletContext());
            } else {
                // Handles both "Download My Records Only" and the custom time-bound forms
                ReportGenerator.generateTimeBoundReport(response.getOutputStream(), currentUser, userId, startDate, endDate, getServletContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error generating PDF report", e);
        }
    }
}
